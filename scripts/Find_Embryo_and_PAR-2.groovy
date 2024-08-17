/* Script to detect embryo(s) and PAR-2 domains
 * 
 * !!! IT WILL DELETE ANY EXISTING INFORMATION !!!
 * 
 * MG_Barbieri_2024
 * 
 * Nicolas Liaudet
 * Bioimaging Core Facility - UNIGE
 * https://www.unige.ch/medecine/bioimaging/en/bioimaging-core-facility/
 * 
 * CC BY-NC 4.0
 * 
 * QuPath 0.5.1
 * 
 * v1.0 30 Apr 2024 NL
*/


import qupath.ext.biop.cellpose.Cellpose2D

setImageType('FLUORESCENCE');
setChannelNames("DIC","eGFP")

selectObjectsByClassification("Embryo","PAR-2")
Objects_To_Remove = getSelectedObjects()
removeObjects(Objects_To_Remove, false)
resetSelection()

ModelName = 'None_DIC.1000'
def pathModel = buildFilePath(PROJECT_BASE_DIR, 'models',ModelName)
//def pathModel = 'cyto2'
def cellpose = Cellpose2D.builder( pathModel )
        .pixelSize( 0.1509 )                  // Resolution for detection in um
//        .channels( "DIC","eGFP" )	               // Select detection channel(s)
               .cellposeChannels(1,0)
//        .preprocess( ImageOps.Filters.median(1) )                // List of preprocessing ImageOps to run on the images before exporting them
//        .normalizePercentilesGlobal(0.1, 99.8, 10) // Convenience global percentile normalization. arguments are percentileMin, percentileMax, dowsample.
//        .tileSize(2048)                  // If your GPU can take it, make larger tiles to process fewer of them. Useful for Omnipose
        .cellprobThreshold(-1)          // Threshold for the mask detection, defaults to 0.0
//        .flowThreshold(1)              // Threshold for the flows, defaults to 0.4
         .diameter(270)                    // Median object diameter. Set to 0.0 for the `bact_omni` model or for automatic computation
        .classify("Embryo")       // PathClass to give newly created objects
        .createAnnotations()             // Make annotations instead of detections. This ignores cellExpansion
        .simplify(0)                     // Simplification 1.6 by default, set to 0 to get the cellpose masks as precisely as possible
        .build()
        
       
def imageData = getCurrentImageData()
ROI = getAnnotationObjects()
cellpose.detectObjects(imageData, ROI)   


selectObjectsByClassification("Embryo");
createAnnotationsFromPixelClassifier("PAR-2_domain_improved", 5.0, 5.0)
anno = getAnnotationObjects().findAll{it->it.getPathClass()==getPathClass('Embryo')||it.getPathClass()==getPathClass('PAR-2')}
anno.each{it->
    it.setLocked(false)
}

