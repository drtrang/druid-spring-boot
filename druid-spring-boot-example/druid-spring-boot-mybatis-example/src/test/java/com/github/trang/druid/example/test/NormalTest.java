package com.github.trang.druid.example.test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.SQLUtils.FormatOption;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import org.junit.Test;

/**
 * @author trang
 */
public class NormalTest {

    @Test
    public void t() throws Exception {
        String sql = "SELECT \n" +
                "  credential.housedel_code \n" +
                "FROM\n" +
                "  sh_house_credential credential, sh_housedel house \n" +
                "WHERE credential.housedel_code = house.housedel_code \n" +
                "  AND credential.status IN (1, 2, 5, 6) \n" +
                "  AND house.del_status NOT IN (0, 7) \n" +
                "  AND credential.id IN \n" +
                "  (SELECT \n" +
                "    MAX(id) \n" +
                "  FROM\n" +
                "    sh_house_credential inCredential, sh_housedel house \n" +
                "  WHERE inCredential.housedel_code = house.housedel_code \n" +
                "    AND inCredential.status != 0 \n" +
                "    AND inCredential.credential_type IN (\n" +
                "      'identify', 'registerPhoto', 'registerDelPhoto', 'purchasePhoto', " +
                "'houseConditionStatement', 'serviceMattersNotice'\n" +
                "    ) \n" +
                "    AND house.del_type = 2 \n" +
                "    AND house.office_address = 110000 \n" +
                "    AND house.del_status NOT IN (0, 7) \n" +
                "  GROUP BY inCredential.housedel_code, inCredential.credential_type) \n" +
                "  AND house.del_type = 2 \n" +
                "  AND house.office_address = 110000 \n" +
                "  AND credential.credential_type IN (\n" +
                "    'identify', 'registerPhoto', 'registerDelPhoto', 'purchasePhoto', " +
                "'houseConditionStatement', 'serviceMattersNotice'\n" +
                "  ) \n" +
                "GROUP BY credential.housedel_code \n" +
                "HAVING COUNT(\n" +
                "    DISTINCT \n" +
                "    CASE\n" +
                "      WHEN credential.credential_type = 'purchasePhoto' \n" +
                "      THEN 'registerPhoto' \n" +
                "      ELSE credential.credential_type \n" +
                "    end\n" +
                "  ) < 5 ;";
        FormatOption option = new FormatOption(VisitorFeature.OutputUCase, VisitorFeature.OutputDesensitize);
        System.out.println(SQLUtils.formatMySql(sql, option));
        System.out.println(SQLUtils.formatMySql(sql, new FormatOption(VisitorFeature.OutputUCase)));
    }

    @Test
    public void hashcode() {
        String camelAlias = "XxXDatasource";
        String alias = camelAlias.substring(0, camelAlias.toLowerCase().indexOf("DataSource".toLowerCase()));
        System.out.println(alias);
    }

}