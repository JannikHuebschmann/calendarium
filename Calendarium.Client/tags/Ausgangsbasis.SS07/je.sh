#
#export JAVA_HOME="/usr/lib/jdk1.3/jre"
#export JAVA_HOME="/usr/lib/jdk1.2.2/jre"
#export JAVA_HOME="/usr/lib/jre1.2.2"
export JAVA_HOME="/usr/lib/jre1.1.7"
#export JAVA_HOME="/usr/lib/jdk1.1.8"
export SWING_HOME="/var/tresor/data/java/Calendarium/swing"
export CALENDARIUM_HOME="/var/tresor/data/java/Calendarium/ServerFS/Calendarium"

export JCP=""
test -r $JAVA_HOME/lib/rt.jar  && JCP="$JCP:$JAVA_HOME/lib/rt.jar"
test -r $JAVA_HOME/lib/classes.zip && JCP="$JCP:$JAVA_HOME/lib/rt.jar"
JCP=`echo "$JCP:$SWING_HOME/swingall.jar:$CALENDARIUM_HOME" |cut -c2-`
echo JCP=$JCP

export JAVA_BIN="$JAVA_HOME/bin/java"
test -x $JAVA_HOME/bin/jre && export JAVA_BIN="$JAVA_HOME/bin/jre"
#echo "
