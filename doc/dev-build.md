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