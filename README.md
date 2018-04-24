# SRDF
SRDF : Lexical Knowledge Graph Extraction Tool for Korean (Open Information Extraction)

## Description

SRDF is the new Korean Open IE system. The system is designed to meet the characteristics of Korean and extract multiple relationships between argument(s) and relation(s) within a sentence by using reificaion technique(specifically, singleton property method).

## How to use

`mvn clean compile exec:java`

## Sample input and output

- Input
문장을 입력하세요: 

세종대왕은 백성들을 위해 훈민정음을 만들었다.


- Output
==NPChunks==

세종대왕 / 은 / 0 / NP_SBJ

백성들 / 을 / 1 / NP_OBJ

훈민정음 / 을 / 3 / NP_OBJ


==VPChunks==

만들 / 었다. / 4 / [2, 3]

위하 / 어 / 2 / [0, 1]



==SRDF Reified Triples==

세종대왕	만들	훈민정음

만들	위하	백성들

====


If you want to modify input and output format specifically (path, console, file, ...), please see the SRDF.java file.

## Licenses

* `CC BY-NC-SA` [Attribution-NonCommercial-ShareAlike](https://creativecommons.org/licenses/by-nc-sa/2.0/)
* If you want to commercialize this resource, [please contact to us](http://mrlab.kaist.ac.kr/contact)

## Papers

If you use the current of PROJECT NAME, please cite the following papers.

* Sangha Nam, Younggyun Hahm, Sejin Nam, Key-Sun Choi, [SRDF: Korean Open Information Extraction using Singleton Property](http://semanticweb.kaist.ac.kr/home/images/f/f5/SRDF_Korean_Open_Information_Extraction_using_Singleton_Property.pdf), Proc. International Semantic Web Conference, ISWC, Posters & Demonstrations Track, 2015. 
* Sangha Nam, GyuHyeon Choi, Key-Sun Choi, [SRDF: A Novel Lexical Knowledge Graph for Whole Sentence Knowledge Extraction](http://semanticweb.kaist.ac.kr/home/images/7/77/SRDF_A_Novel_Lexical_Knowledge_Graph_for_Whole_Sentence_Knowledge_Extraction.pdf), LDK 2017: Language, Data, and Knowledge pp 315-329, 2017. 

## Contact
`nam.sangha at gmail.com`

## Acknowledgement
This work was supported by Institute for Information & communications Technology Promotion(IITP) grant funded by the Korea government(MSIT) (2013-0-00109, WiseKB: Big data based self-evolving knowledge base and reasoning platform)
