# Welcome to Expressive RDF Mapper (XRM) repository

This is the main code repository for the XRM project.

The XRM editor allows you to write data mappings to RDF in a friendly domain specific language (DSL) and generates output in
[R2RML](http://www.w3.org/TR/r2rml/), [RML](https://rml.io/specs/rml/), [CARML](https://github.com/carml/carml) and [CSV on the Web](https://w3c.github.io/csvw/primer/) format.


## Installation and usage

For further information, including installation and usage instructions of XRM, see the user-oriented [expressive-rdf-mapper](https://github.com/zazuko/expressive-rdf-mapper) repository.

## Command line (no Eclipse)

The DSL can be validated and compiled from the command line, e.g. for CI or coding agents.

With Docker (no Java needed), using the published image `ghcr.io/zazuko/xrm`:

```sh
bin/xrm generate path/to/project      # writes path/to/project/src-gen/
bin/xrm generate -o out path/to/project
bin/xrm validate path/to/project      # check only, no output
```

Or with Java 11+, using `xrm-cli-<version>.jar` from the [GitHub releases](https://github.com/zazuko/xrm/releases):

```sh
java -jar xrm-cli.jar generate path/to/project
```

The latest build of each branch is available at a stable URL (pre-release `<branch>-snapshot`):

```sh
curl -LO https://github.com/zazuko/xrm/releases/download/main-snapshot/xrm-cli.jar
```

All `*.xrm` files below the given paths are loaded together, so references across files (vocabularies, sources) resolve like in Eclipse. Output file names match what the Eclipse plugin produces. Problems are reported as `file:line:column: severity: message`; files with errors are skipped. The exit code is `1` if there were errors.

`bin/xrm` mounts the current directory into the container, so paths given to it must be inside the current directory. To use a locally built image: `docker build -t xrm . && XRM_IMAGE=xrm bin/xrm ...`.
