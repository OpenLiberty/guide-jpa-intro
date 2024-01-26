#!/bin/bash
while getopts t:d:b:u:j: flag;
do
    case "${flag}" in
        t) DATE="${OPTARG}" ;;
        d) DRIVER="${OPTARG}" ;;
        j) JDK_LEVEL="${OPTARG}" ;;
        *) echo "Invalid option input" ;;
    esac
done

if [ "$JDK_LEVEL" == "11" ]; then
    echo "Test skipped because the guide does not support Java 11."
    exit 0
fi

sed -i "\#<artifactId>liberty-maven-plugin</artifactId>#a<configuration><install><runtimeUrl>https://public.dhe.ibm.com/ibmdl/export/pub/software/openliberty/runtime/nightly/$DATE/$DRIVER</runtimeUrl></install></configuration>" frontendUI/pom.xml
cat frontendUI/pom.xml

sed -i "\#<artifactId>liberty-maven-plugin</artifactId>#,\#<configuration>#c<artifactId>liberty-maven-plugin</artifactId><version>3.10</version><configuration><install><runtimeUrl>https://public.dhe.ibm.com/ibmdl/export/pub/software/openliberty/runtime/nightly/$DATE/$DRIVER</runtimeUrl></install>" backendServices/pom.xml
cat backendServices/pom.xml

../scripts/testApp.sh
