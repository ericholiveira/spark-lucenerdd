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
package org.zouzias.spark.lucenerdd.config

import org.apache.lucene.analysis._
import org.apache.lucene.analysis.ar.ArabicAnalyzer
import org.apache.lucene.analysis.bg.BulgarianAnalyzer
import org.apache.lucene.analysis.br.BrazilianAnalyzer
import org.apache.lucene.analysis.ca.CatalanAnalyzer
import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.analysis.ckb.SoraniAnalyzer
import org.apache.lucene.analysis.core.WhitespaceAnalyzer
import org.apache.lucene.analysis.cz.CzechAnalyzer
import org.apache.lucene.analysis.da.DanishAnalyzer
import org.apache.lucene.analysis.de.GermanAnalyzer
import org.apache.lucene.analysis.el.GreekAnalyzer
import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.analysis.es.SpanishAnalyzer
import org.apache.lucene.analysis.eu.BasqueAnalyzer
import org.apache.lucene.analysis.fa.PersianAnalyzer
import org.apache.lucene.analysis.fi.FinnishAnalyzer
import org.apache.lucene.analysis.fr.FrenchAnalyzer
import org.apache.lucene.analysis.ga.IrishAnalyzer
import org.apache.lucene.analysis.gl.GalicianAnalyzer
import org.apache.lucene.analysis.hi.HindiAnalyzer
import org.apache.lucene.analysis.hu.HungarianAnalyzer
import org.apache.lucene.analysis.id.IndonesianAnalyzer
import org.apache.lucene.analysis.it.ItalianAnalyzer
import org.apache.lucene.analysis.lt.LithuanianAnalyzer
import org.apache.lucene.analysis.lv.LatvianAnalyzer
import org.apache.lucene.analysis.nl.DutchAnalyzer
import org.apache.lucene.analysis.no.NorwegianAnalyzer
import org.apache.lucene.analysis.pt.PortugueseAnalyzer
import org.apache.lucene.analysis.ru.RussianAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tr.TurkishAnalyzer
import org.apache.lucene.index.IndexOptions
import org.apache.lucene.search.similarities.{BM25Similarity, ClassicSimilarity, Similarity}
import org.locationtech.spatial4j.io.ShapeIO

/**
 * Configuration parameters for LuceneRDD
 */
case class LuceneRDDParams(
  // Basic parameters
  maxTopKValue: Int = 1000,
  defaultTopK: Int = 10,
  defaultFacetNum: Int = 10,
  stringFieldsAnalyzed: Boolean = true,
  nonAnalyzedFields: List[String] = List.empty,
  storeTermVectors: Boolean = true,
  storeTermPositions: Boolean = true,
  omitNorms: Boolean = false,
  indexOptions: IndexOptions = IndexOptions.DOCS_AND_FREQS_AND_POSITIONS,
  linkerMethod: String = "collectbroadcast",

  // Analyzer and similarity parameters
  indexAnalyzerName: String = "standard",
  queryAnalyzerName: String = "standard",
  similarityName: String = "classic",
  indexAnalyzerPerFieldNames: Map[String, String] = Map.empty,
  queryAnalyzerPerFieldNames: Map[String, String] = Map.empty,

  // Shape parameters
  prefixTreeMaxLevel: Int = 11,
  prefixTreeName: String = "geohash",
  prefixTreeMaxDistErr: Double = 1.0,
  locationFieldName: String = "__location__",
  shapeFormat: String = ShapeIO.WKT,
  shapeLinkerMethod: String = "collectbroadcast",

  // Storage parameters
  indexStoreMode: String = "disk") extends Serializable {

  // For backward compatibility
  def indexAnalyzer: String = indexAnalyzerName
  def queryAnalyzer: String = queryAnalyzerName
  def similarity: String = similarityName
  def indexAnalyzerPerField: Map[String, String] = indexAnalyzerPerFieldNames
  def queryAnalyzerPerField: Map[String, String] = queryAnalyzerPerFieldNames

  def getAnalyzerByName(name: String): Analyzer = {
    name match {
      case "whitespace" => new WhitespaceAnalyzer()
      case "ar" => new ArabicAnalyzer()
      case "bg" => new BulgarianAnalyzer()
      case "br" => new BrazilianAnalyzer()
      case "ca" => new CatalanAnalyzer()
      case "cjk" => new CJKAnalyzer()
      case "ckb" => new SoraniAnalyzer()
      case "cz" => new CzechAnalyzer()
      case "da" => new DanishAnalyzer()
      case "de" => new GermanAnalyzer()
      case "el" => new GreekAnalyzer()
      case "en" => new EnglishAnalyzer()
      case "es" => new SpanishAnalyzer()
      case "eu" => new BasqueAnalyzer()
      case "fa" => new PersianAnalyzer()
      case "fi" => new FinnishAnalyzer()
      case "fr" => new FrenchAnalyzer()
      case "ga" => new IrishAnalyzer()
      case "gl" => new GalicianAnalyzer()
      case "hi" => new HindiAnalyzer()
      case "hu" => new HungarianAnalyzer()
      case "id" => new IndonesianAnalyzer()
      case "it" => new ItalianAnalyzer()
      case "lt" => new LithuanianAnalyzer()
      case "lv" => new LatvianAnalyzer()
      case "nl" => new DutchAnalyzer()
      case "no" => new NorwegianAnalyzer()
      case "pt" => new PortugueseAnalyzer()
      case "ru" => new RussianAnalyzer()
      case "tr" => new TurkishAnalyzer()
      case _ => new StandardAnalyzer()
    }
  }

  def getSimilarityByName(name: String): Similarity = {
    name.toLowerCase match {
      case "bm25" => new BM25Similarity()
      case _ => new ClassicSimilarity()
    }
  }

  def getIndexAnalyzer: Analyzer = getAnalyzerByName(indexAnalyzerName)
  def getQueryAnalyzer: Analyzer = getAnalyzerByName(queryAnalyzerName)
  def getSimilarityImpl: Similarity = getSimilarityByName(similarityName)
}
