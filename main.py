import tabula
import os
import glob
import json
import time
import csv
import re

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
filesJson = []
for file in files:
    print("\nProcessing file : \"" + file + "\"")
    print("Enter page numbers of tables you wish to extract. \nPage numbers can be a single number (ie 8), or can be a range (ie 8-9)\nFor multiple tables, you will need to run this tool again\nAlternativley, type \"skip\" to skip this file")
    inp = input("")
    if(inp.lower() == "skip"):
       continue
    filesJson.append(tabula.read_pdf(file, pages = inp, multiple_tables=True, output_format="json"))

#Now filesJson conains json data for each table that was extracted

count = 0
for jsonFile in filesJson:
    fileName = "tableJson"
    if(count != 0):
        fileName += count
    
    with open(fileName + ".json", "w") as outFile:
        json.dump(jsonFile, outFile)

#Read the JSON we just made in order to convert it to a .csv
dataWeWant = []

jsonStrings = []
files = glob.glob("*.json")
for file in files:
    f = open(file)
    jsonStringBS = f.readlines()
    jsonStringBS = re.sub("[\n\r]","",jsonStringBS[0])
    data = json.loads(jsonStringBS)
    for i in data:
        jsonStrings.append(i["data"])

dataWeWant = []
for arrays in jsonStrings:
    for array in arrays:
        dataFromTable = []
        for string in array:
            dataFromTable.append(string["text"])
        dataWeWant.append(dataFromTable)

print("Formatting csv")

#CSVStuff
csvHeaders = ["Subject", "Start Date", "Start Time", "End Date", "End Time", "All day event", "Description", "Location"]
csvData = []

for row in dataWeWant:
    csvRowData = []
    try:
        if(row[0] == "" or row[0] == "DATE"):
            continue
        desc = "Readings: "
        desc += re.sub("[\n\r]"," ",row[2])
        desc += " | Assignments: "
        desc += re.sub("[\n\r]"," ",row[3])

    #Convert row[0] into a date that google calendar likes (DD/MM/YYYY)
    
    # Aug 25
    # 25/08/2021
        s = re.sub("[\n\r]", " ", row[0]).split(" ")
        date = s[1]
    except:
        continue
    sub = ""
    if(s[0] == "Jan"):
        sub = "1"
    elif(s[0] == "Feb"):
        sub = "2"
    elif(s[0] == "Mar"):
        sub = "3"
    elif(s[0] == "Apr"):
        sub = "4"
    elif(s[0] == "May"):
        sub = "5"
    elif(s[0] == "Jun"):
        sub = "6"
    elif(s[0] == "Jul"):
        sub = "7"
    elif(s[0] == "Aug"):
        sub = "8"
    elif(s[0] == "Sept"):
        sub = "9"
    elif(s[0] == "Oct"):
        sub = "10"
    elif(s[0] == "Nov"):
        sub = "11"
    elif(s[0] == "Dec"):
        sub = "12"
    
    date += "/" + sub + "/2021"
    
    #Append data
    csvRowData.append(re.sub("[\n\r]"," ",row[1]))
    csvRowData.append(date)
    csvRowData.append("")
    csvRowData.append(date)
    csvRowData.append("")
    csvRowData.append("TRUE")
    csvRowData.append(desc)
    csvRowData.append("")

    #Append to the correct thing
    csvData.append(csvRowData)

with open("calendarData.csv", "w") as f:
    writer = csv.writer(f)
    writer.writerow(csvHeaders)
    writer.writerows(csvData)

#Clean up
print("Removing excess files...")
files = glob.glob("*.json")
for file in files:
    os.remove(file)

print("Done!")
print("Take calendarData.csv and upload that to google calendar")