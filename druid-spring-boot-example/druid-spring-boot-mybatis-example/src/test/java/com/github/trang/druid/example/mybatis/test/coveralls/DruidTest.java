package com.github.trang.druid.example.mybatis.test.coveralls;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.SQLUtils.FormatOption;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * DruidTest
 *
 * @author trang
 */
@Slf4j
public class DruidTest {

    @Test
    public void encrypt() throws Exception {
        String origin = "123456";
        String encrypt = ConfigTools.encrypt(origin);
        String decrypt = ConfigTools.decrypt(ConfigTools.DEFAULT_PUBLIC_KEY_STRING, encrypt);
        assertEquals(origin, decrypt);
    }

    @Test
    public void format() {
        String sql = "SELECT id,task_id,task_source, housedel_code, del_type, office_address, company_code, brand, " +
                "class1_code, class2_code, class2_name, status, create_time, end_time, creator_ucid, creator_name, " +
                "org_code, prove_id, prove_time, audit_ucid, audit_name, audit_reject_reason, audit_content, " +
                "audit_time, pass_mode, sms_content, sms_time, lianjia_app_content, lianjia_app_time \r\n FROM " +
                "sh_true_house_task \r\n  WHERE office_address = 0 AND status = 0 ORDER     BY id DESC";
        System.out.println(sql);
        System.out.println(SQLUtils.formatMySql(sql));
        System.out.println(SQLUtils.formatMySql(sql, new FormatOption(VisitorFeature.OutputUCase)));
    }

}