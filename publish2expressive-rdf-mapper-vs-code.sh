#!/bin/bash
cp -r com.zazuko.rdfmapping.dsl.parent/com.zazuko.rdfmapping.{dsl,dsl.tests,dsl.ui,dsl.ui.tests,dsl.ide} ../expressive-rdf-mapper-vscode/

vsEditorTestFolder="../expressive-rdf-mapper-vscode/demo/editortest"
if [ ! -d $vsEditorTestFolder ] ; then
  mkdir $vsEditorTestFolder
fi
cp -r runtime-EclipseXtext/editor-test/*.xrm $vsEditorTestFolder
