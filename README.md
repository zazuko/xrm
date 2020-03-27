# DSL for RDF Mapping

A domain specific language (DSL) for mapping data to RDF. It includes a generator for R2RML.

Note: Language and generators are still work-in-progress and subject to change.

# Install Instruction (local build & installation)

1. clone the github repository
2. `mvn clean verify` (inside the *com.zazuko.rdfmapping.dsl.parent* module)
3. Open Eclipse
4. Help (Menu Item) -> Install New Software
5. Add (button)
6. Local (button) and select the map "com.zazuko.rdfmapping.dsl.parent\com.zazuko.rdfmapping.dsl.repository\target\repository"
7. Give the repository you are creating some name.
8. Select in the textbox "Work with" the newly created repository.
9. In the tree view "Zazuko" will be visible under this category select "RdfMapping DSL".
10. Next (button)
11. Finish
12. Eclipse will show an warning that it is a unsigned plugin. Click on the "Install anyway" button.
13. Restart Eclipse.


# Publishing a new version

Update version and run local build, inside the *com.zazuko.rdfmapping.dsl.parent* module:

    mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=0.11.0-SNAPSHOT
    mvn clean verify

Trigger gitlab to publish a release on https://download.zazukoians.org/rdf-mapping-dsl/releases/:

    git tag 0.11.0 master
    git push; git push origin 0.11.0
    
Hints for deleting tags (just in case ..):
    
    git tag -d 1.0.0
    git tag -l
    git push --delete origin 1.0.0

For end-user composite updatesite, see [repository-composite](com.zazuko.rdfmapping.dsl.parent/com.zazuko.rdfmapping.dsl.repository-composite/)
