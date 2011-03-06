Persistent Store Plugin
=======================

A plugin for PhoneGap BlackBerry WebWorks that implements BlackBerry's PersistentStore.

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

Copy

    ./plugin/build/www/ext/store.jar

To

    c:/my-app/www/ext/store.jar

### 3. Install Web Assets

Copy

    ./plugin/build/www/javascript/store.js

To _(anywhere inside your project's `www` directory)_

    c:/my-app/www/javascript/store.js

### 4. Include the JavaScript

    <script type="text/javascript" src="javascript/store.js"></script>

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
