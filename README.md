#rawabi

Source code delivered for Rawabi Telecom under contract to Beachdog Networks.

##Build

```bash
$ cd pcs-db-core
$ mv install
```

##Install

# prepaid local callflow

## Build

```bash
$ cd pcs-db-core
$ mvn install
```

## Install

### Callflow
The callflow changes for this feature have been made to callingcard_411.xml, which is checked in under the `xtml-apps/apps` folder.  Simply copy that version into `$PS_ROOT/apps` of the application server(s).  

### Drop to Java

The modified callflow implemented by callingcard_411.xml uses a new drop-to-java method to register an ANI.  This method is contained in the `pcs-db-core-1.0.jar` file built by the `pcs-db-core` maven project. 
To generate that file, follow the build instructions above.  The built jar file should be deployed into the `$PS_APPS_ROOT/java` directory on the application server.

Note that `pcs-db-core-1.0.jar` file is dependent on some third-party jar files, and these should be deployed into the `$PS_APPS_ROOT/jarfiles` directory on the application server.  The list of dependencies can be obtained from the pom.xml file, or a full list of the jars can be accessed at `/export/home/pcs3/kit/callflow-jarfiles` on rw-psas-01.

Also note that `pcs-db-core-1.0.jar` file is dependent on a beans.xml file and a log4j.properties file being deployed into the `$PS_APPS_ROOT/java` directory on the application server.  The former provides database connectivity information while the latter provides log settings.  For examples of these files please see `/export/home/pcs3/pactolus_applications/java` on rw-psas-01.

A special note should be made about setting the Java CLASSPATH variable, since that is a common source of problems.  The jars mentioned above (`pcs-db-core-1.0.jar` plus the third party jars) must all appear in the Java classpath, of course.  Typically, this is set in the ~pcs/.bashrc file.  The easiest way to do it is simply to include lines similar to that below to include all of the jar files in the relevant directories, rather than manually adding them one by one to the classpath:

```bash
export CLASSPATH=${CLASSPATH}:${PS_APPS_ROOT}/java

for f in $PS_APPS_ROOT/java/*.jar
do
export CLASSPATH=${CLASSPATH}:$f
done

for f in $PS_APPS_ROOT/jarfiles/*.jar
do
export CLASSPATH=${CLASSPATH}:$f
done
```




