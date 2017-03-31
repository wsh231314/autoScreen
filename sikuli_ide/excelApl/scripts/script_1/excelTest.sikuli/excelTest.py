click("1489138223735.png")
wait("1489129207946.png")
doubleClick("1489129226971.png")
wait("1489129256160.png")
click("1489129274165.png")
type('www.baidu.com')
click("1489138257601.png")

import xlrd
data = xlrd.open_workbook('C:/Users/shawn.shaohua.wang/Desktop/tmp/excelFile.xls')
sheet = data.sheets()[0]
for i in range(sheet.nrows):

    
    value = sheet.row(i)[0].value
    click("1489129756772.png")
    type(value)
    click("1489129779583.png")
    click("1489130098432.png")

