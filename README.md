# QBO-Inventory
Scan upc code and update Quickbook Online inventory

This app scans a bar code, checks upcdatabase.org for product information and updates item quantity in Intuit QuickBooks Online. The bar code is stored as SKU field in QuickBooks Online inventory.


Prerequisite:
You need to have an app created in appcenter.intuit.com. No need to be published.
You need to have set of intuit consumer key/secret and access token/secret
You need to have signed up and a key from upcdatabase.org
You then create a config file with name qbo.txt in this format:


consumerKey:{value}

consumerSecret:{value}

accessToken:{value}

accessTokenSecret:{value}

upcDatabaseKey:{value}

QBOCompanyId:{value}

sampleItemName:{value}


Sample item name is the name of an item in your QuickBooks Online inventory that tracks quantity and has asset, income, expense account setup.

This file needs to be placed on your phone sd card under /sdcard/.

The app once installed will read this file to load keys into setting.
