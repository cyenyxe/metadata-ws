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
package uk.ac.ebi.ampt2d.metadata.parser;

import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ResourceUtils;
import uk.ac.ebi.ampt2d.metadata.enaobject.EnaObjectCollector;
import uk.ac.ebi.ampt2d.metadata.service.EnaDbService;
import uk.ac.ebi.ena.sra.xml.AnalysisFileType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource("classpath:application.properties")

public class EnaObjectCollectorTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Mock
    private SQLXML sqlxml;

    @Mock
    private EnaDbService enaDbService;

    @InjectMocks
    private EnaObjectCollector enaObjectCollector;

    @Before
    public void initialization() throws Exception {
        List<SQLXML> sqlxmlList = new ArrayList<>();
        sqlxmlList.add(sqlxml);
        when(sqlxml.getString()).thenReturn(getXmlFile("classpath:ERZ000011.xml"));
        when(jdbcTemplate.queryForObject(anyString(), anyMap(), eq(Long.class))).thenReturn(Long.valueOf(5));
        when(jdbcTemplate.queryForList(anyString(), anyMap(), eq(SQLXML.class))).thenReturn(sqlxmlList);
        when(enaDbService.getEnaAnalysisXml()).thenReturn(sqlxmlList);
    }

    private String getXmlFile(String fileName) throws IOException {
        File file = ResourceUtils.getFile(fileName);
        return new String(Files.readAllBytes(file.toPath()));
    }

    @Test
    public void testGetEnaAnalysisFileTypeFromDb() {
        List<AnalysisFileType> analysisFileTypeList = enaObjectCollector.getEnaAnalysisFileTypeFromDb();
        assertAnalysisFileTypeList(analysisFileTypeList);
    }

    private void assertAnalysisFileTypeList(List<AnalysisFileType> analysisFileList) {
        assertEquals(analysisFileList.size(), 2);
        assertEquals(analysisFileList.get(0).getFilename(), "UK10K_SCOOP5013826.vcf.gz");
        assertEquals(analysisFileList.get(0).getFiletype(), AnalysisFileType.Filetype.Enum.forInt(AnalysisFileType.Filetype.INT_VCF));
        assertEquals(analysisFileList.get(0).getChecksum(), "980aad09354c5bc984e23d2f74efdf3b");
        assertEquals(analysisFileList.get(0).getChecksumMethod(), AnalysisFileType.ChecksumMethod.Enum.forString("MD5"));
        assertEquals(analysisFileList.get(1).getFilename(), "ERZ000/ERZ000001/do131_Input_liver_none_mmuC57BL65_CRI01.sra.sorted.bam");
        assertEquals(analysisFileList.get(1).getFiletype(), AnalysisFileType.Filetype.Enum.forInt(AnalysisFileType.Filetype.INT_BAM));
        assertEquals(analysisFileList.get(1).getChecksum(), "15191d68bdd5c1ad23c943c3da3730c7");
        assertEquals(analysisFileList.get(1).getChecksumMethod(), AnalysisFileType.ChecksumMethod.Enum.forString("MD5"));
    }

    @Test(expected = XmlException.class)
    public void testAnalysisFileParserWrongInput() throws Exception {
        String xmlStr = "/<?wrong xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ANALYSIS_SET>\n" +
                "  <ANALYSIS alias=\"uk10k_scoop5013826.vcf.gz-vcf_analysis-sc-20120330\" center_name=\"SC\" broker_name=\"EGA\" analysis_center=\"SC\" accession=\"ERZ000011\">\n" +
                "  </ANALYSIS>\n" +
                "</ANALYSIS_SET>\n";
        AnalysisFileTypeFromXml analysisFileTypeFromSet = new AnalysisFileTypeFromXml();
        analysisFileTypeFromSet.extractFromXml(xmlStr);
    }

}
