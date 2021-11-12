package xyz.zenheart.guiserial.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.stereotype.Component;
import xyz.zenheart.guiserial.pojo.entity.UserEntity;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: TODO </p>
 * <p>创建时间: 2021/9/16 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
@Component
public class FtlUtils {

    private static Configuration configuration;

    public static void main(String[] args) {
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

        cfg.setClassForTemplateLoading(FtlUtils.class, PathUtils.FTL);

        cfg.setDefaultEncoding("UTF-8");

        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        try {
            Template temp = cfg.getTemplate("entity.java.ftl");
            System.out.println(temp);
            FileWriter fileWriter = new FileWriter("entity.java");
            UserEntity userEntity = new UserEntity();
            userEntity.setName("zhangsan");
            userEntity.setPwd("123456");
            temp.process(userEntity, fileWriter);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(Object o, String templateName, String fileName) {
        Template temp = null;
        try {
            temp = configuration.getTemplate(templateName);
            System.out.println(temp);
            FileWriter fileWriter = new FileWriter(fileName);
            temp.process(o, fileWriter);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    @Resource
    public void afterPropertiesSet(Configuration configuration) {
        FtlUtils.configuration = configuration;
    }
}
