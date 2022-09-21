# ontology-version-extractor [![Build & Test](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/build.yml/badge.svg)](https://github.com/psiotwo/ontology-version-extractor/actions/workflows/build.yml)
A heuristic-based extractor of ontology version without downloading the whole ontology.

This is a simple tool to extract versions from web ontologies (assuming that whenever a new version is released, their version number is also updated).

Use-case: Checking, which ontologies need updating.

## Usage

Currently configured for OBO ontologies:

`gradle run output.csv` will generate `output.csv` with list of OBO ontologies and their current versions.
