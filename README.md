# R&R planning reader
R&R planning reader is a simple tool to read and parse R&R planning files as JSON. These files are exported by the R&R planning tool in .pdf format.
In order to make the outputted .pdf files readable by the tool, they first have to be converted to .xlsx files. This can be done by using any pdf-to-excel converter.
However, the tool was tested with the online tool [adobe pdf to excel online](https://www.adobe.com/nl/acrobat/online/pdf-to-excel.html).

The JSON output can then be processed by any tool that can read JSON files. We are currently using a Django web application to display the data.
But also a script to convert the JSON to a way better readable format is possible. 

The tool is compiled and tested using JAVA 17 LTS. It is recommended to use the same version. However, it should also work with other versions.

## Usage

Proper Usage is: `java -jar RRreader.jar filename.xlsx`

### Adaptations
The program defaults to commonly used default values for column headers. If you are using different column names, make sure to modify the `START_CELL_STRINGS` array in the `PdfReader.java` file.


## Sample output
```json
[
  {
    "datum":"2022-05-10",
    "achternaam":"Jansen",
    "eind":"17:00",
    "pauze":"01:00",
    "voornaam":"Jeroen",
    "begin":"08:00",
    "colors":["#008000","#FFFF00"]
  }
]
```

## Contributing
If there are any issues, feel free to open an issue. If you have any interesting additions or optimizations you can open a pull request.

## Example input file
The input PDF consists of multiple pages looking like shown below, each representing different departments.
Large part of the data is masked for privacy reasons, but one should be able to grasp the general idea.
![Example planning file](/imgs/RR-screenshot.png)
