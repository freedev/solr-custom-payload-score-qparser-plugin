# Solr Custom Payload Score QParser Plugin

This custom Query Parser, based on the standard `payload_score`, let you add two new parameters to the former: `slop` and `inOrder`, this let you handle queries with terms in any order and that matches only few terms.

Example document:

```
{
    "id":"1",
    "multiPayload": [
      "A|1 B|2 C|3 D|4",
      "A|0.1 B|0.2 E|5 F|6",
      "E|0.5 F|0.6"
    ]
}
```

Example field definition

```
  <fieldType name="payloads" stored="true" indexed="true" class="solr.TextField" positionIncrementGap="1000">
    <analyzer type="index">
      <tokenizer class="solr.WhitespaceTokenizerFactory"/>
      <filter class="solr.DelimitedPayloadTokenFilterFactory" encoder="float"/>
    </analyzer>
    <analyzer type="query">
      <tokenizer class="solr.WhitespaceTokenizerFactory"/>
    </analyzer>
  </fieldType>

<field name="multiPayload" type="payloads" indexed="true" stored="true" multiValued="true" />
```

Try using
```
{!custom_payload_score f=multipayload v=$pl func=sum includeSpanScore=false operator=phrase slop=100 inOrder=false}`
```

For example:
```
http://localhost:8983/solr/test/select?debugQuery=true&q={!payload_score f=multiPayload v=$pl func=sum includeSpanScore=false operator=phrase slop=100 inOrder=false}&pl=A B
```

## Requirements
- Solr 11.x
- A field type that utilizes payloads string

## Building
Building requires JDK 8 and Maven.  Once you're setup just run:

`mvn package` to generate the latest jar in the target folder.

## Todo
- Add test unit

## Usage - Configuration steps

- copy the `solr-custom-payload-score-qparser-plugin-1.0.jar` into your solr_home/dist directory
- add these lines in your `solrconfig.xml`:

```
  <queryParser name="custom_payload_score" class="it.damore.solr.payload.CustomPayloadScoreQParserPlugin">
  </queryParser>

<lib dir="${solr.install.dir:../../../..}/dist/" regex="solr-custom-payload-score-qparser-plugin-\d.*\.jar" />
<lib dir="${solr.solr.home}/lib/" regex="solr-custom-payload-score-qparser-plugin-\d.*\.jar" />
```
