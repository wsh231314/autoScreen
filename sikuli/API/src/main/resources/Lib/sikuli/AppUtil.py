from Sikuli import *
from sys import argv
from xlsxwriter import *
import time
from sikuli import GlobalVar

def captureNow():
    
    #Evidence picture File
    image_path = None
    if len(argv) > 1:
        image_path = JScreen.all().getRegion().saveScreenCapture(argv[1])
    else:
        image_path = JScreen.all().getRegion().saveScreenCapture()
        
    #Add image to excel
    evidence_excel = GlobalVar.get_value('evidence_excel')
    work_sheet = evidence_excel.worksheets()[0]
    row = GlobalVar.get_value('row')
    work_sheet.insert_image(row, 1, image_path)
    row = row + 50
    GlobalVar.set_value('row',row);
    GlobalVar.set_value('evidence_excel',evidence_excel);
        
def initEvidence():
    # base path setting
    basepath = ''
    if len(argv) > 1:
        basepath = argv[1]
    else:
        basepath = RUNTIME.fpBaseTempPath + '/'
        
    #file name
    now_time = time.time()
    local_time = time.localtime(now_time)
    now_string = time.strftime('%Y%m%d%H%M%S', local_time)
    filename_xls = now_string + '.xlsx'
    evidence_excel_path = basepath + filename_xls
    
    #create excel
    evidence_excel = Workbook(evidence_excel_path)
    evidence_excel.add_worksheet('page1')
    
    GlobalVar._init()
    GlobalVar.set_value('evidence_excel',evidence_excel);
    GlobalVar.set_value('row',1);
    
def endEvidence():
    evidence_excel = GlobalVar.get_value('evidence_excel')
    evidence_excel.close()
