The respective code is in a dedicated repository: https://github.com/zazuko/expressive-rdf-mapper-vscode

Running the VS Code extension, I made the following observations:
* generators work
* validations work
* code formatter works
* keyword escaping works
* partial syntax highlighting (it's custom made)
* code assist does not propose terminals (BLOCK_BEGIN, BLOCK_END, LINE_END)
* code assist proposes in some places all strings of the file, for example at BLOCK_BEGIN position
* code assist rules implemented in the DSL are neglected, semantically inappropriate proposals are shown
* quickfixes implemented in the DSL are missing

For details, see https://github.com/zazuko/expressive-rdf-mapper-vscode/issues/1