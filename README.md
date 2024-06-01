# How to use the API
 -  `/api/v1/enrich` accepts multipart form data with a key `file`
 - Curl example: curl -F file=@trade.csv http://localhost:8080/api/v1/enrich

# Discussion / Comments on the design

Don't see any reason of using multithreading here as we already upload a huge (up to 10^6 rows) file into RAM.
Due to that reason it seems to be fine to statically upload file with product name mappings into RAM as well.
As we iterate over inputstream, there is no reason to split it into chunks for multiple threads, so we can simply 
iterate over input file and do replacements.

# Any ideas for improvement if there were more time available.
I'm not a fan of doing such long test tasks, so I'll just leave comments what can be / should be improved 
in real-task-case scenario.

 - Add validation for file format either by simple check of `endsWith(".csv")` or more complex one with 
checking the first bytes of the file to determine it's real format. (e.g. in case we send a PDF file with name 'myCsv.csv')
 - Add lambda/interface/common code which can work with different file formats and produce e.g. `line` to work with, so
then we can have a common code of validation/name remapping and have different implementations to work with different file formats.
 - Improve error handling and do not return HTTP 500, but it depends on interaction with client.
 - Add mapper from file to object, so we don't work with `parts[i]` each time to obtain file info
 - Add tests