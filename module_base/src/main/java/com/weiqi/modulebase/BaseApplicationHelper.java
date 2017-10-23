package com.weiqi.modulebase;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.weiqi.modulebase.manager.AccountManager;
import com.weiqi.modulebase.moduleinterface.ModuleConfig;
import com.weiqi.modulebase.moduleinterface.ModuleManager;
import com.weiqi.modulebase.moduleinterface.provider.ICatProvider;
import com.weiqi.modulebase.moduleinterface.provider.IDogProvider;
import com.weiqi.modulebase.moduleinterface.provider.IHomeProvider;
import com.weiqi.modulebase.util.ContextUtil;
import com.weiqi.modulebase.util.SDUtils;
import com.weiqi.modulebase.widget.ToastUtil;
import com.weiqi.slog.SLog;
import com.weiqi.slog.SLogConstants;
import com.weiqi.slog.Settings;
import com.weiqi.slog.formatter.DefaultConsoleFormatter;
import com.weiqi.slog.formatter.DefaultFileFormatter;
import com.weiqi.slog.printer.DefaultConsolePrinter;
import com.weiqi.slog.printer.DefaultFilePrinter;
import com.weiqi.slog.printer.Printer;
import com.weiqi.slog.util.LogLevel;
import com.weiqi.slog.util.LogSegment;
import com.weiqi.slog.util.ZoneOffset;

import java.util.ArrayList;

/**
 * 公共 初始化
 * Created by alexwangweiqi on 17/9/19.
 */

public class BaseApplicationHelper {

    public static void init(boolean debug, Application context) {

        boolean isRunAlone = BuildConfig.isRunAlone;

        initLog(debug, context);
        SLog.d("BaseAppHelper","debug:"+ debug+" isRunAlone:"+isRunAlone);

        if (debug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(context);

        ContextUtil.init(isRunAlone, context);

        ToastUtil.init(context);
        AccountManager.init();

        ModuleConfig moduleConfig = new ModuleConfig.ModuleBuilder()
                .addModule(IHomeProvider.HOME_SERVICE, IHomeProvider.HOME_SERVICE)
                .addModule(ICatProvider.CAT_SERVICE, ICatProvider.CAT_SERVICE)
                .addModule(IDogProvider.DOG_SERVICE, IDogProvider.DOG_SERVICE)
                .build();
        ModuleManager.getInstance().init(moduleConfig);

//        EventBus.builder().addIndex(new EvIndex()).installDefaultEventBus();

    }

    private static void initLog(boolean debug, Context context) {
        //init log
        Settings settings = new Settings.Builder()
                .context(context.getApplicationContext())//获取设备信息等写到日志文件头部
                .mLogSegment(LogSegment.TWENTY_FOUR_HOURS)//保存日志文件时间切片 如果缓存日志量大可以使用小时间片
                .zoneOffset(ZoneOffset.P0800)//保存日志时区偏移
                .timeFormat(SLogConstants.DEFAULT_TIME_FORMAT)//保存日志时间格式
                .isBorder(true)//是否 开启外框
                .isThread(true)//是否 打印线程信息
                .isStackTrace(true)//是否 打印堆栈跟踪信息 非必要可以关闭 提升性能
                .build();

        /**
         * 创建一个控制台打印机
         */
        Printer consolePrinter = new DefaultConsolePrinter();
        /**
         * 设置需要的堆栈跟踪信息深度为2层并开启数据自动JSON格式化
         */
        consolePrinter.setFormatter(new DefaultConsoleFormatter(3, true));

        /**
         * 创建一个SD卡文件打印机 设置日志存储地址
         * 默认开启 一个文件上限为30的缓存清理工具 你也可以null关闭 或者自定义实现
         */
        Printer filePrinter = new DefaultFilePrinter(SDUtils.getLogPath(context.getApplicationContext()));
        filePrinter.setFormatter(new DefaultFileFormatter());
        filePrinter.addLevelForFile(new ArrayList<LogLevel>() {//需要写入文件的日志类型 不设置默认全写入日志文件
            {
                add(LogLevel.WTF);
            }
        });

        if (debug) {
            SLog.init(settings, consolePrinter, filePrinter);
        } else {
            SLog.init(settings, filePrinter);//非debug环境 关闭consolePrinter
        }
    }
}
