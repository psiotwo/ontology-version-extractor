# ontology-version-extractor [![Build & Test](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/build.yml/badge.svg)](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/build.yml) [![](https://jitpack.io/v/psiotwo/ontology-version-extractor.svg)](https://jitpack.io/#psiotwo/ontology-version-extractor) [![Update OBO Ontology Version](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/update-obo-status.yml/badge.svg)](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/update-obo-status.yml)
This is a simple tool to extract versions from web ontologies, based on fetching and analysing a starting snippet of the ontology for some version-related information.

Use-case: Checking, which ontologies need updating.

## Latest OBO ontology versions

Go to https://psiotwo.github.io/ontology-version-extractor/output.csv.html

## Usage

Currently configured for OBO ontologies:

`gradle run output.csv` will generate `output.csv` with list of OBO ontologies and their current versions.
