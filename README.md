# ontology-version-extractor [![Build & Test](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/build.yml/badge.svg)](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/build.yml) [![](https://jitpack.io/v/psiotwo/ontology-version-extractor.svg)](https://jitpack.io/#psiotwo/ontology-version-extractor) [![Update OBO Ontology Version](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/update-obo-status.yml/badge.svg)](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/update-obo-status.yml)
This is a simple tool to extract versions from web ontologies, based on fetching and analysing a starting snippet of the ontology for some version-related information.

Use-case: Checking, which ontologies need to be updated in a local catalog.

## Latest OBO ontology versions

See the [HTML report](https://psiotwo.github.io/ontology-version-extractor/output.html), download the [RDF data](https://psiotwo.github.io/ontology-version-extractor/output.ttl) or [CSV data](https://psiotwo.github.io/ontology-version-extractor/output.csv).

## Usage

Currently configured for OBO ontologies:

- `gradle run --args="extract -o output.ttl` will generate `output.ttl` - a Turtle representation of OBO ontology headers and their current versions.
- `gradle run --args="transform -i input.ttl -o output.csv` will generate `output.csv` - a CSV report with the list of OBO ontologies and their current versions.
- `gradle run --args="transform -i input.ttl -o output.html` will generate `output.html` - an HTML report with the list of OBO ontologies and their current versions.
