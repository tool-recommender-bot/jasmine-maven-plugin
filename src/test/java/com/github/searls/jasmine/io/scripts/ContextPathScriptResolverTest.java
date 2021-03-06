/*-
 * #%L
 * jasmine-maven-plugin
 * %%
 * Copyright (C) 2010 - 2017 Justin Searls
 * %%
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
 * #L%
 */
package com.github.searls.jasmine.io.scripts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContextPathScriptResolverTest {

  private static final String ROOT_CONTEXT_PATH = "";
  private static final String SOURCE_CONTEXT_PATH = "src";
  private static final String SPEC_CONTEXT_PATH = "spec";

  private static final String BASE_DIRECTORY = "/the/base/directory";
  private static final String SOURCE_DIRECTORY = "/the/source/directory";
  private static final String SPEC_DIRECTORY = "/the/spec/directory";

  @Mock
  private ScriptResolver scriptResolver;

  private ScriptResolver contextPathScriptResolver;

  @Before
  public void before() {
    this.contextPathScriptResolver = new ContextPathScriptResolver(
      scriptResolver,
      SOURCE_CONTEXT_PATH,
      SPEC_CONTEXT_PATH
    );
  }

  @Test
  public void testGetSourceDirectory() {
    assertThat(this.contextPathScriptResolver.getSourceDirectory()).isEqualTo(SOURCE_CONTEXT_PATH);
  }

  @Test
  public void testGetSpecDirectory() {
    assertThat(this.contextPathScriptResolver.getSpecDirectory()).isEqualTo(SPEC_CONTEXT_PATH);
  }

  @Test
  public void testGetSources() {
    String[] scripts = new String[]{"scriptA", "scriptB", "lib/scriptC"};

    when(this.scriptResolver.getSourceDirectory()).thenReturn(SOURCE_DIRECTORY);
    when(this.scriptResolver.getSources()).thenReturn(setOf(SOURCE_DIRECTORY, scripts));

    Set<String> expected = setOf(SOURCE_CONTEXT_PATH, scripts);

    assertThat(this.contextPathScriptResolver.getSources()).isEqualTo(expected);
  }

  @Test
  public void testGetSpecs() {
    String[] scripts = new String[]{"scriptA", "scriptB", "lib/scriptC"};

    when(this.scriptResolver.getSpecDirectory()).thenReturn(SPEC_DIRECTORY);
    when(this.scriptResolver.getSpecs()).thenReturn(setOf(SPEC_DIRECTORY, scripts));

    Set<String> expected = setOf(SPEC_CONTEXT_PATH, scripts);

    assertThat(this.contextPathScriptResolver.getSpecs()).isEqualTo(expected);
  }

  @Test
  public void testGetPreloads() {
    Set<String> preloads = new HashSet<>();
    preloads.add(BASE_DIRECTORY + "/lib/baseScript");
    preloads.add(SPEC_DIRECTORY + "/specScript");
    preloads.add(SOURCE_DIRECTORY + "/sourceScript");
    preloads.add("http://example.org/script.js");

    when(this.scriptResolver.getBaseDirectory()).thenReturn(BASE_DIRECTORY);
    when(this.scriptResolver.getSourceDirectory()).thenReturn(SOURCE_DIRECTORY);
    when(this.scriptResolver.getSpecDirectory()).thenReturn(SPEC_DIRECTORY);
    when(this.scriptResolver.getPreloads()).thenReturn(preloads);

    Set<String> expected = new HashSet<>();
    expected.add(ROOT_CONTEXT_PATH + "/lib/baseScript");
    expected.add(SPEC_CONTEXT_PATH + "/specScript");
    expected.add(SOURCE_CONTEXT_PATH + "/sourceScript");
    expected.add("http://example.org/script.js");

    assertThat(this.contextPathScriptResolver.getPreloads()).isEqualTo(expected);
  }

  private static Set<String> setOf(String base, String... strings) {
    Set<String> set = new HashSet<>();
    for (String string : strings) {
      set.add(base + "/" + string);
    }
    return set;
  }

}
