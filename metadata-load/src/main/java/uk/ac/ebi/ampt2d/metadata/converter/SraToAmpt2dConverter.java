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
package uk.ac.ebi.ampt2d.metadata.converter;

import java.util.List;
import java.util.stream.Collectors;

public interface SraToAmpt2dConverter<SOURCE, DESTINATION> {

    DESTINATION convert(SOURCE source);

    default List<DESTINATION> convertModels(List<SOURCE> sources) {
        return sources.stream().map(source -> convert(source)).collect(Collectors.toList());
    }
}
