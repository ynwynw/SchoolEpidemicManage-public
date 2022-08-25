package com.fanchen;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Arrays;

//代码自动生成器
public class CodeGenerator {

    public static void main(String[] args) {
        //代码自动生成器对象
        AutoGenerator generator = new AutoGenerator();
        //1.全局配置
        GlobalConfig config = new GlobalConfig();
        String project_path = System.getProperty("user.dir");//获取项目文件夹目录
        config.setOutputDir(project_path + "/src/main/java");//设置生成目录
        config.setAuthor("fanchen");
        config.setOpen(false);//是否打开资源管理器
        config.setFileOverride(false);//是否覆盖
        config.setServiceName("%sService");//去Service前缀I
        config.setIdType(IdType.AUTO);//主键类型
        config.setDateType(DateType.ONLY_DATE);//设置日期格式
        config.setSwagger2(true);//开启Swagger
        generator.setGlobalConfig(config);
        //2.设置数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("fanchen520");
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/system_admin?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false");
        dataSourceConfig.setDbType(DbType.MYSQL);
        generator.setDataSource(dataSourceConfig);
        //3.包的配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.fanchen");
        packageConfig.setEntity("entity");
        packageConfig.setMapper("mapper");
        packageConfig.setService("service");
        packageConfig.setController("controller");
        generator.setPackageInfo(packageConfig);
        //4.策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setInclude("sys_notice");//设置要映射的表名
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(true);//自动Lombok
        strategyConfig.setLogicDeleteFieldName("is_delete");//逻辑删除
        TableFill gmtCreate = new TableFill("create_time", FieldFill.INSERT);//插入自动生成时间 配置
        TableFill gmtModified = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        strategyConfig.setTableFillList(Arrays.asList(gmtCreate, gmtModified));
        strategyConfig.setVersionFieldName("version");//乐观锁
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setControllerMappingHyphenStyle(true);//localhost:8080/hello_id_2
        generator.setStrategy(strategyConfig);
        //5.执行
        generator.execute();
    }

}
