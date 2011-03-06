PhoneGap Hashpipe Plugin
========================

> Store JSON with the platform's SDK.

A PhoneGap plugin to create a hash table database using the platform's SDK. The concept is similar to [DOM Storage / Web Storage](https://developer.mozilla.org/en/dom/storage), with the bonus of no database size restriction.

Supports Lawnchair using the BlackBerry PersistentStore adapter.

Supported PhoneGap Platforms
----------------------------

- BlackBerry WebWorks (5.0 & 6.0)

Directory Structure
-------------------

    build.xml ....... Easily build and deploy the plugin
    plugin/ ......... PersistentStore plugin implementation
    plugin/src/ ..... Java implementation
    plugin/www/ ..... Web implementation
    test/ ........... Test application using Lawnchair's test-suite

Using the Plugin with your Project
----------------------------------

### 1. Build the plugin

    ant build

### 2. Install Java Library

Copy `./plugin/build/www/ext/hashpipe.jar`

To `c:/my-app/www/ext/hashpipe.jar`

### 3. Install Web Assets

Copy `./plugin/build/www/javascript/hashpipe.js`

To `c:/my-app/www/javascript/hashpipe.js`

### 4. Include the JavaScript

    <script type="text/javascript" src="javascript/hashpipe.js"></script>

### 5. Create Lawnchair Instance

    <script type="text/javascript">
        document.addEventListener('deviceready', function() {
            window.adapter = 'blackberry';

            try {
                new Lawnchair({adaptor:window.adapter}, function(){
                    var database = this;
                    // database.get(...);
                    // database.save(...);
                });
            }
            catch(e) {
                alert(e);
            }
        }, false);
    </script>

Running the Tests
-----------------

### Configuration

Specify your SDK location in `test/project.properties`:

    bbwp.dir=C:\\Program Files (x86)\\Research In Motion\\BlackBerry Widget Packager

### Build and Deploy

    ant help
    ant load-device     # build plugin and run tests on a device
    ant load-simulator  # build plugin and run tests on a simulator

Plugin Development
------------------

### Java

    plugin/src/com/phonegap/plugins

### Web

    plugin/www
