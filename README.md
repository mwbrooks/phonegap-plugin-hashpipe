Persistent Store Plugin
=======================

A plugin for PhoneGap BlackBerry WebWorks that implements BlackBerry's PersistentStore.

Directory Structure
-------------------

    build.xml ....... Easily build and deploy the plugin
    plugin/ ......... PersistentStore plugin implementation
    plugin/src/ ..... Java implementation
    plugin/www/ ..... Web implementation
    test/ ........... Test application
    test/www/ ....... Web implementation
    test/www/ext/ ... BlackBerry Java implementation

Build and Deploy Plugin
-----------------------

### Configuration

Specify your SDK location in `test/project.properties`:

    bbwp.dir=C:\\Program Files (x86)\\Research In Motion\\BlackBerry Widget Packager

### Build and Deploy

    ant help
    ant load-device     # build/install plugin, build/install app to device
    ant load-simulator  # build/install plugin, build/install app to simulator

Plugin Development
------------------

### Java

Edit the Java source code in `plugin/src/com/phonegap/plugins`

### JavaScript / Web Assets

Do whatever you need in `plugin/www`