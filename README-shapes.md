# Expressive RDF Mapper (XRM) - Shapes Prototype


## Installation instructions for the Shapes Prototype


### Using Eclipse

1) Download and install Eclipse: https://www.eclipse.org/downloads/packages/
   * Recommended package: *Eclipse IDE for Java Developers* (tested with the Eclipse IDE 2021-06 R Package. It already includes a suitable JRE)

2) Start Eclipse and install the extension:
   * Help > Install New Software
   * Click `Add..` to add a new repository and fill in the following details:
     * Name: *XRM Shapes Prototype*
     * Location: https://download.zazukoians.org/xrm-shacl-prototype/releases/latest/
   * Select *Expressive RDF Mapper (XRM) - Shapes Prototype* from the list and click `Next>`
   * Confirm the security warning about unsigned content by clicking on `Install anyway`

If you receive an error message about missing dependencies, then make sure that the option *"Contact all update sites during install to find required software"* is selected in the install dialog.


## Shapes examples

For examples, have a look at the [person-shapes](runtime-EclipseXtext/person-shapes) project folder.

1. Clone this repository.
2. In Eclipse, click _File > Import_.
3. In the Import wizard: Expand General and then click _Projects from Folder or Archive_ . Click Next . Click _Directory_ and select the `runtime-EclipseXtext/person-shapes` project folder. Click Finish to add it to your Eclipse workspace.
4. Open the file `person-shapes.xrm` and try to modify the current mapping.
5. Xtext constantly watches the `.xrm` files in your Eclipse project and validates the syntax (ie. *on save*). When the syntax is valid, it automatically writes the generated file(s) to the `./src-gen` folder.


## License and Support

The included [End-User License Agreement](EULA.md) covers personal, non-commercial use.

For commercial use, including commercial support, consult our [product page](https://zazuko.com/products/expressive-rdf-mapper/) at zazuko.com for more details.

Please report issues and feature requests on Github. If you have other questions please post a message in the [RDF.community discussion forum](https://discuss.rdf.community/).
