/* Export all annotations
* 
* MG_Barbieri_2024
*
* Nicolas Liaudet
* Bioimaging Core Facility
* University of Geneva
*
* CC BY-NC 4.0
* 
* QuPath 0.5.1
* 
* v1.0 NL 30-Apr-2024
*
*/


//************************************************************************
//**************** Get current image name and output path ****************
//************************************************************************
def path = buildFilePath(PROJECT_BASE_DIR, 'Annotations Export')
mkdirs(path)
//Get current image name
String imageData = getCurrentImageData()
//println(describe(getCurrentImageData()))
//nameOfActiveImage = getProjectEntry().getImageName()
String ImageType = getCurrentImageData().getImageType()
FileName = imageData.split(ImageType+', ')[1]
FileName = FileName.split('.nd2')[0]

//****************************************************
//**************** Export annotations ****************
//****************************************************
boolean prettyPrint = false 
def gson = GsonTools.getInstance(prettyPrint)
def annotations = getAnnotationObjects()
outfname = buildFilePath(path,FileName+'.json')
File file = new File(outfname)
file.withWriter('UTF-8') {
    gson.toJson(annotations,it)
}
