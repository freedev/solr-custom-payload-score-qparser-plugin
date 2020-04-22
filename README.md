# Solr Custom Payload Score QParser Plugin

This custom Query Parser is based on the standard `payload_score` but add two parameters `slop` and `inOrder`, this let you handle queries with terms in any order and that matches only few terms.

Example document:

```
{
    "id":"1",
    "multiPayload": [
      "A:1 B:2 C:3 D:4",
      "A:0.1 B:0.2 E:5 F:6",
      "E:0.5 F:0.6"
    ]
}
```

Try using `{!custom_payload_score f=multipayload v=$pl func=sum includeSpanScore=false operator=phrase slop=100 inOrder=false}`

Try using `http://localhost:8983/solr/test/select?debugQuery=true&q={!payload_score f=multipayload v=$pl func=sum includeSpanScore=false operator=phrase}&pl=__MY_CLAUSES__`:

Then change `__MY_CLAUSES__` with `A B`

```

```


## Requirements
- Solr 7.x
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
<lib dir="${solr.install.dir:../../../..}/dist/" regex="solr-custom-payload-score-qparser-plugin-\d.*\.jar" />
<lib dir="${solr.solr.home}/lib/" regex="solr-custom-payload-score-qparser-plugin-\d.*\.jar" />
```
