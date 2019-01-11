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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import uk.ac.ebi.ena.sra.xml.AnalysisFileType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class AnalysisFileTypeFromXmlTest {

    private String getXmlFile(String fileName) throws IOException {
        File file = ResourceUtils.getFile(fileName);
        return new String(Files.readAllBytes(file.toPath()));
    }

    @Test
    public void testAnalysisFileParser() throws Exception {
        String xmlStr = getXmlFile("classpath:ERZ000011.xml");
        AnalysisFileTypeFromXml analysisFileTypeFromSet = new AnalysisFileTypeFromXml();
        List<AnalysisFileType> analysisFileList = analysisFileTypeFromSet.extractFromXml(xmlStr);
        assertAnalysisFileList(analysisFileList);
    }

    private void assertAnalysisFileList(List<AnalysisFileType> analysisFileList) {
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

}
