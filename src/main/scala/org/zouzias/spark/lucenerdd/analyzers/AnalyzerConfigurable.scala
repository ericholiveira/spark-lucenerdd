/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zouzias.spark.lucenerdd.analyzers

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.zouzias.spark.lucenerdd.config.Configurable
import org.apache.spark.internal.Logging

/**
 * Lucene Analyzer loader via configuration
 *
 * An analyzer can be loaded by using all the short country codes, i.e.,
 * en,el,de, etc or using a class name present in the classpath, i.e.,
 * 'org.apache.lucene.analysis.el.GreekAnalyzer'
 */
trait AnalyzerConfigurable extends Configurable with Logging {

  /** Get the configured analyzers or fallback to English */
  protected def getOrElseEn(analyzerName: Option[String]): String = analyzerName.getOrElse("en")

  protected val IndexAnalyzerConfigName: Option[String] = Some(params.indexAnalyzerName)
  protected val QueryAnalyzerConfigName: Option[String] = Some(params.queryAnalyzerName)

  /** Load custom analyzer using reflection */
  private def loadConstructor[T <: org.apache.lucene.analysis.Analyzer](className: String): T = {
    val loader = getClass.getClassLoader
    logInfo(s"Loading class ${className} using loader ${loader}")
    val loadedClass: Class[T] = loader.loadClass(className).asInstanceOf[Class[T]]
    val constructor = loadedClass.getConstructor()
    constructor.newInstance()
  }

  /** Get analyzer instance from name */
  protected def getAnalyzer(analyzerName: Option[String]): org.apache.lucene.analysis.Analyzer = {
    if (analyzerName.isDefined) {
      try {
        // Try to load custom analyzer first
        loadConstructor[org.apache.lucene.analysis.Analyzer](analyzerName.get)
      }
      catch {
        case _: Throwable =>
          // If custom analyzer fails, use the built-in analyzer from LuceneRDDParams
          params.getAnalyzerByName(analyzerName.get)
      }
    }
    else {
      logInfo("Analyzer name is not defined. Default analyzer is StandardAnalyzer().")
      new StandardAnalyzer()
    }
  }
}
