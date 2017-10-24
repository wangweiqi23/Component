package com.weiqi.plugin

import com.android.builder.internal.ClassFieldImpl
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileTree

/**
 * Created by alexwangweiqi on 17/10/20.
 */

class BuildPlugin implements Plugin<Project> {

    static final String MAP_SEPARATOR = "#"

    static final String DEBUG = "debug"
    static final String DEBUG_COMPILE = "debugCompile"
    static final String RELEASE = "release"
    static final String RELEASE_COMPILE = "releaseCompile"

    static final String IS_APP_BUILD = "isAppBuild"
    static final String BUILD_AAR_STABLE = "stable"

    //默认是app，直接运行assembleRelease的时候，等同于运行app:assembleRelease
    String compilemodule = "app"

    //是否自动更新aar：false为自动更新
    boolean aarStable = false

    void apply(Project project) {
        String taskNames = project.gradle.startParameter.taskNames.toString()
        System.out.println("taskNames is " + taskNames);
        String module = project.path.replace(":", "")
        System.out.println("current module is " + module);
        AssembleTask assembleTask = getTaskInfo(project.gradle.startParameter.taskNames)


        if (project.hasProperty(BUILD_AAR_STABLE)) {
            aarStable = true
            println("stop auto build aar")
        }

        if (assembleTask.isAssemble || assembleTask.isBuild) {
            fetchMainmodulename(project, assembleTask);
            System.out.println("compilemodule  is " + compilemodule);
        }

        if (!project.hasProperty("isRunAlone")) {
            throw new RuntimeException("you should set isRunAlone in " + module + "'s gradle.properties")
        }

        //对于isRunAlone==true的情况需要根据实际情况修改其值，
        // 但如果是false，则不用修改，该module作为一个lib，运行module:assembleRelease则发布aar到中央仓库
        boolean isRunAlone = Boolean.parseBoolean((project.properties.get("isRunAlone")))
        String mainmodulename = project.rootProject.property("mainmodulename")
        if (isRunAlone && (assembleTask.isAssemble || assembleTask.isBuild
                || assembleTask.isTinkerPatch)) {
            //对于要编译的组件和主项目，isRunAlone修改为true，其他组件都强制修改为false
            //这就意味着组件不能引用主项目，这在层级结构里面也是这么规定的
            if (module.equals(compilemodule) || module.equals(mainmodulename)) {
                isRunAlone = true;
            } else {
                isRunAlone = false;
            }
        }

        project.setProperty("isRunAlone", isRunAlone)

        if (assembleTask.isClean) {
            FileTree tree = project.fileTree(dir: "../componentrelease/")
            tree.include '**/*.aar'
            tree.each {
                File file ->
                    file.delete()
            }
            println("clean componentrelease files :" + tree.dir.absolutePath);
        }

        //根据配置添加各种组件依赖，并且自动化生成组件加载代码
        if (isRunAlone) {
            project.apply plugin: 'com.android.application'
            System.out.println("apply plugin is " + 'com.android.application');
            if ((assembleTask.isAssemble || assembleTask.isBuild || assembleTask.isTinkerPatch)
                    && module.equals(compilemodule)) {
                compileComponents(assembleTask, project)
            }
        } else {
            project.apply plugin: 'com.android.library'
            System.out.println("apply plugin is " + 'com.android.library');
            def array = [DEBUG, RELEASE]
            project.afterEvaluate {
                array.each { type ->
                    Task task = project.tasks.findByPath(getTaskName(type))
                    if (task != null) {
                        task.doLast {
                            File infile = project.file("build/outputs/aar/" + module + "-" + type + ".aar")
                            File outfile = project.file("../componentrelease")
                            File desFile = project.file(module + "-" + type + ".aar");
                            project.copy {
                                from infile
                                into outfile
                                rename {
                                    String fileName -> desFile.name
                                }
                            }
                            System.out.println("$module-" + type + ".aar copy success ");
                        }
                    }
                }
            }
        }


        if ((assembleTask.isAssemble || assembleTask.isBuild) && project.hasProperty(IS_APP_BUILD)) {
            boolean isApp = compilemodule.equals(mainmodulename)
            println("get project isAppBuild: " + project.property("isAppBuild") + " isApp " + isApp.toString());
            project.setProperty(IS_APP_BUILD, isApp)
            project.extensions.android.buildTypes.all { buildType ->
                addBuildConfigField(new ClassFieldImpl("boolean", IS_APP_BUILD, isApp.toString()))
            }
            println("set isAppBuild " + isApp.toString());
        }


    }

    /**
     * 根据当前的task，获取要运行的组件，规则如下：
     * assembleRelease ---app
     * app:assembleRelease :app:assembleRelease ---app
     * sharecomponent:assembleRelease :sharecomponent:assembleRelease ---sharecomponent
     * @param assembleTask
     */
    private void fetchMainmodulename(Project project, AssembleTask assembleTask) {
        if (!project.rootProject.hasProperty("mainmodulename")) {
            throw new RuntimeException("you should set compilemodule in rootproject's gradle.properties")
        }
        if (assembleTask.modules.size() > 0 && assembleTask.modules.get(0) != null
                && assembleTask.modules.get(0).trim().length() > 0
                && !assembleTask.modules.get(0).equals("all")) {
            compilemodule = assembleTask.modules.get(0);
        } else {
            compilemodule = project.rootProject.property("mainmodulename")
        }
        if (compilemodule == null || compilemodule.trim().length() <= 0) {
            compilemodule = "app"
        }
    }

    private AssembleTask getTaskInfo(List<String> taskNames) {
        AssembleTask assembleTask = new AssembleTask();
        for (String task : taskNames) {
            if (task.toUpperCase().contains("ASSEMBLE")
                    || task.contains("aR")
                    || task.toUpperCase().contains("RESGUARD")) {
                if (task.toUpperCase().contains("DEBUG")) {
                    assembleTask.isDebug = true;
                }
                assembleTask.isAssemble = true;
                String[] strs = task.split(":")
                assembleTask.modules.add(strs.length > 1 ? strs[strs.length - 2] : "all");
                break;
            } else if (task.toUpperCase().contains("BUILD")) {
                assembleTask.isBuild = true;
                String[] strs = task.split(":")
                assembleTask.modules.add(strs.length > 1 ? strs[strs.length - 2] : "all");
            } else if (task.toUpperCase().contains("TINKERPATCH")) {
                assembleTask.isTinkerPatch = true;
            } else if (task.toUpperCase().contains("CLEAN")) {
                assembleTask.isClean = true;
            }
        }
        return assembleTask
    }

    /**
     * 自动添加依赖，只在运行assemble任务的才会添加依赖，因此在开发期间组件之间是完全感知不到的，这是做到完全隔离的关键
     * 支持两种语法：module或者modulePackage:module,前者之间引用module工程，后者使用componentrelease中已经发布的aar
     * @param assembleTask
     * @param project
     */
    private void compileComponents(AssembleTask assembleTask, Project project) {
        String debugComponents;
        String releaseComponents;

        debugComponents = (String) project.properties.get("compileComponent")
        releaseComponents = (String) project.properties.get("compileComponent")

        if (assembleTask.isBuild || assembleTask.isTinkerPatch) {
            addDebugDependency(project, debugComponents)
            addReleaseDependency(project, releaseComponents)

        } else if (assembleTask.isDebug) {
            addDebugDependency(project, debugComponents)

        } else {
            addReleaseDependency(project, releaseComponents)

        }

    }

    private void addDebugDependency(Project project, String debugComponents) {
        if (debugComponents == null || debugComponents.length() == 0) {
            println("no debug dependencies")
        } else {
            String[] debugCompArray = debugComponents.split(",")
            for (String dependency : debugCompArray) {
                addDependency(project, dependency, DEBUG_COMPILE, DEBUG)
            }
        }
    }

    private void addReleaseDependency(Project project, String releaseComponents) {
        if (releaseComponents == null || releaseComponents.length() == 0) {
            println("no release dependencies")
        } else {
            String[] releaseCompArray = releaseComponents.split(",")
            for (String dependency : releaseCompArray) {
                addDependency(project, dependency, RELEASE_COMPILE, RELEASE)
            }
        }
    }


    private void addDependency(Project project, String dependency,
                               String compileType, String compileConfiguration) {
        println("comp is " + dependency)
        if (dependency.contains(":")) {//依赖 module
            project.dependencies.add(compileType,
                    project.dependencies.project(path: dependency, configuration: compileConfiguration))
            println("add dependency project: " + dependency + ", build type:" + compileConfiguration);

        } else {//依赖 module aar
            String module = dependency
            println("add dependency " + module + " aar")
            long lastModifiedModuleAarFile = 0
            long lastModifiedCompilerAarFile = 0

            long lastModifiedModuleDir = getModuleLastModified(project, module)

            File compileAarFile = project.file(getComponentAarFilePath(module, compileConfiguration))
            if (compileAarFile.exists()) {
                lastModifiedCompilerAarFile = compileAarFile.lastModified()
            }

            File moduleAarFile = project.file(getModuleAarFilePath(module, compileConfiguration))
            if (moduleAarFile.exists()) {
                lastModifiedModuleAarFile = moduleAarFile.lastModified()
            }

            println("add dependency module aar: " + dependency + ", build type:" + compileConfiguration + " lastModifiedModuleDir:" + lastModifiedModuleDir
                    + " lastModifiedModuleAarFile:" + lastModifiedModuleAarFile + " lastModifiedCompilerAarFile:" + lastModifiedCompilerAarFile);

            if (lastModifiedCompilerAarFile > 0 && lastModifiedCompilerAarFile > lastModifiedModuleDir) {
                project.dependencies.add(compileType, ':' + module + "-" + compileConfiguration + "@aar")
                println("add dependency : " + ':' + module + "-" + compileConfiguration + "@aar")
                return
            } else if (lastModifiedModuleAarFile > lastModifiedModuleDir && moduleAarFile.exists()) {
                //检查该module目录下是否已生成aar，已生成复制到componentrelease下

                //未生成aar，依赖module工程
                if (compileAarFile.exists()) {
                    compileAarFile.delete()
                }

                File outfile = project.file("../componentrelease")
                project.copy {
                    from moduleAarFile
                    into outfile
                    rename {
                        String fileName -> moduleAarFile.name
                    }
                }
                project.dependencies.add(compileType, ':' + module + "-" + compileConfiguration + "@aar")
                println("copy to component; add dependency " + ':' + module + "-" + compileConfiguration + "@aar");
                return
            }

            //未生成aar，依赖module工程
            if (compileAarFile.exists()) {
                compileAarFile.delete()
            }
            project.dependencies.add(compileType,
                    project.dependencies.project(path: ':' + module, configuration: compileConfiguration))
            println("aar not exist, add  $compileConfiguration dependency project: " + module);

//            HashMap oldMap = new HashMap()
//            HashMap newMap = new HashMap()
//            File aarFile = project.file(getComponentAarFilePath(module, compileConfiguration))

//            if (!aarStable) {
//                oldMap = new HashMap()
//                File hashFile = project.file(getHashFilePath(module, compileConfiguration))
//                if (hashFile.exists()) {
//                    oldMap = parseMap(hashFile)
//                    hashFile.write("")
//                } else {
//                    hashFile.createNewFile()
//                }
//
//                newMap = getHashMap(project, module, hashFile)
//
//                println("$compileConfiguration module $module change: " + !newMap.equals(oldMap))
//
//            }
//
//            if (aarStable || newMap.equals(oldMap)) {
//                if (aarFile.exists()) {
//                    project.dependencies.add(compileType, dependency + "-" + compileConfiguration + "@aar")
//                    println("add dependency : " + dependency + "-" + compileConfiguration + "@aar");
//                    return
//                } else {
//                    // 检查该module目录下是否已生成aar，已生成复制到componentrelease下
//                    File infile = project.file(getModuleAarFilePath(module, compileConfiguration))
//                    if (infile.exists()) {
//                        File outfile = project.file("../componentrelease")
//                        File desFile = project.file(module + "-" + compileConfiguration + ".aar");
//                        project.copy {
//                            from infile
//                            into outfile
//                            rename {
//                                String fileName -> desFile.name
//                            }
//                        }
//                        project.dependencies.add(compileType, dependency + "-" + compileConfiguration + "@aar")
//                        println("copy to component; add dependency : "
//                                + dependency + "-" + compileConfiguration + "@aar");
//                        return
//                    }
//
//                }
//            }
//
//            //未生成aar，依赖module工程
//            if (aarFile.exists()) {
//                aarFile.delete()
//            }
//            project.dependencies.add(compileType,
//                    project.dependencies.project(path: ':' + module, configuration: compileConfiguration))
//            println("aar not exist, add  $compileConfiguration dependency project: " + module);
        }
    }

    private String getHashFilePath(String module, String type) {
        return "../" + module + "/build/outputs/hash_" + type + ".txt"
    }

    private String getComponentAarFilePath(String module, String type) {
        return "../componentrelease/" + module + "-" + type + ".aar"
    }

    private String getModuleAarFilePath(String module, String type) {
        return "../" + module + "/build/outputs/aar/" + module + "-" + type + ".aar"
    }

    private long getModuleLastModified(Project project, String module) {
        long datetime = 0
        File moduleDir = project.file("../" + module + "/")
        datetime = moduleDir.lastModified()
        File[] fileList = moduleDir.listFiles()
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory() && file.lastModified() > datetime) {
                    datetime = f.lastModified()
                }
            }
        }
        return datetime
    }

    private String getTaskName(String type) {
        StringBuilder builder = new StringBuilder("assemble")
        switch (type) {
            case DEBUG:
                builder.append("Debug")
                break
            case RELEASE:
                builder.append("Release")
                break
        }
        return builder.toString()
    }

    private String getFileHash(File file) {
        FileInputStream inputStream = new FileInputStream(file)
        String hash = DigestUtils.shaHex(inputStream)
        inputStream.close()
        return hash
    }

    private HashMap getHashMap(Project project, String module, File hashFile) {
        HashMap map = new HashMap()

        if (hashFile.exists()) {
            File dir = project.file("../" + module + "/src/main")
            dir.eachFileRecurse(
                    { file ->
                        if (!file.isDirectory()) {
                            def filePath = file.getPath()
                            def fileHash = getFileHash(file)

                            map.put(filePath, fileHash)
                            hashFile.append(format(filePath, fileHash))
                        }
                    }
            )
        }

        return map
    }

    private HashMap parseMap(File hashFile) {
        def hashMap = [:]
        if (hashFile.exists()) {
            hashFile.eachLine {
                List list = it.split(MAP_SEPARATOR)
                if (list.size() == 2) {
                    hashMap.put(list[0], list[1])
                }
            }
        } else {
            println "$hashFile does not exist"
        }
        return hashMap
    }

    private String format(String path, String hash) {
        return path + MAP_SEPARATOR + hash + "\n"
    }

    private class AssembleTask {
        boolean isAssemble = false;
        boolean isBuild = false;
        boolean isDebug = false;
        boolean isTinkerPatch = false;
        boolean isClean = false;
        List<String> modules = new ArrayList<>();
    }
}