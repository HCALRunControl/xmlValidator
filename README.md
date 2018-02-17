hcalfmXMLvalidator
==================
Validates master and grandmaster snippets by using the parsing code of the [HCAL Function Manager](https://github.com/HCALRunControl/levelOneHCALFM).

Usage
-----
The xmlValidator creates a copy of the xml file to validate in a temporary directory. The copy has the CfgCVS structure that the HCALFM is used to seeing; i.e. if your xml file is called `my_xml_file.xml`, creates a copy with the structure `my_xml_file.xml/pro`. By default, this copy will be made in the `/tmp` directory, but you can specify a different location.

To validate an xml file,
```java -jar hcalfmXMLvalidator.jar my_xml_file.xml```


To specify that the temporary directory should be somewhere else besides `/tmp`,
```java -jar hcalfmXMLvalidator.jar my_xml_file.xml my_tmp_directory```


Getting and Compiling
---------------------
On a machine where RCMS and the HCALFM are installed,
```git clone git@github.com:HCALRunControl/xmlValidator.git
cd xmlValidator
ant
```
The executable jar file will appear in the `jars` directory. It should be able to run on any machine that has java installed.

Note that it should always be recompiled against the up-to-date FM code.

Note also that currently it needs to be built against the branch `refactorXML` in the HCALFM repository (until it gets merged).
