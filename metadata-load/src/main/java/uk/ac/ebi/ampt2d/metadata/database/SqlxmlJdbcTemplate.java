/*
 *
 * Copyright 2018 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package uk.ac.ebi.ampt2d.metadata.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLXML;
import java.util.List;

public class SqlxmlJdbcTemplate {

    private static final Logger logger = LoggerFactory.getLogger(SqlxmlJdbcTemplate.class);

    private JdbcTemplate jdbcTemplate;
    private String sql;
    private String column;

    public SqlxmlJdbcTemplate(DataSource dataSource, String sql, String column) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.sql = sql;
        this.column = column;
    }

    public List<SQLXML> listSqlxml() {
        List<SQLXML> sqlxmlList;
        sqlxmlList = jdbcTemplate.query(sql, new SqlxmlMapper(column));
        return sqlxmlList;
    }

}
