import json
import os
import glob
import time
import numpy
import re
# import opencv-python
import pytesseract
from PIL import Image
import tabula

#Get a list of all files in current dir
print("Finding .pdf files")
print(os.getcwd())
files = glob.glob("*.pdf")

#Check that we got some files
if(len(files) == 0):
    print("No .pdf files found!\nMove files to same folder as \"main.py\" (this file)\nExiting...")
    exit()

print("\nFound following files:")
for file in files:
    print(file)

print("\nIf all the files you want to search through are not there, press \"Ctrl + C\" now, and move files to same folder as \"main.py\" (this file)")
time.sleep(3)
print("Continuing...")
time.sleep(0.1)

dataWeWant = []

#Start gathering data from files
for file in files:
    cols = {"Subject": -1, "Date": -1, "Description": -1}
    print("Starting work on file " + file)
    pages = input("Enter pages on PDF \""+ file + "\" where the table exists\neg 8, 8-9, all: ")

    #For each heading in the table
    for key in cols:
        print("Enter the column number where the " + key + " is\n(Left to Right, with 1 being the 1st column in the table):")
        while(True):
            num = input("")
            if(num.isnumeric()):
                num = int(num)
                if(num > 0):
                    num -= 1
                    cols[key] = num
                    break
            print("Unrecognised input")
    print("Finished getting columns")

    readJson = tabula.read_pdf(file, pages = pages, output_format="json")[0]
    newJsonString = str(readJson).replace("'", '"')
    jsonStrings = []
    
    data = json.loads(newJsonString)["data"]
    for i in data:
        jsonStrings.append(i)

    thisData = []
    for row in jsonStrings:
        tmp = []
        tmp.append(row[cols["Subject"]])
        tmp.append(row[cols["Date"]])
        tmp.append(row[cols["Description"]])
        thisData.append(tmp)

    #Filter data before we add to thisData

    #TODO

print(dataWeWant)

print("Formatting csv")
#CSVStuff
csvHeaders = ["Subject", "Start Date", "Start Time", "End Date", "End Time", "All day event", "Description", "Location"]
csvData = []

print("Done!")
print("Take calendarData.csv and upload that to google calendar")