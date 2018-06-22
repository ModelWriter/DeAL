Building Jikes RVM
==================
1. sudo apt-get install g++-5-multilib

2. install apache-ant-1.6.5-bin.tar.gz

3. Set JAVA_HOME

jdk-1_5_0_22-linux-amd64.bin


4. Configure checkstyle download location.
5. install Java jdk 1.6.0_31
6. ant -Dconfig.name=BaseBaseGCAssertions -Dhost.name=x86_64-linux -Dcomponents.cache.dir=/home/ferhat/DeAL/temp
7. Change build.xml
    <mkdir dir="${build.base}/syscall/java"/>
    <exec executable="/usr/lib/jvm/jdk1.5.0_22/bin/apt" failonerror="true">
      <arg value="-factorypath"/>
      <arg value="${tasks.classes}"/>
      <arg value="-nocompile"/>
      <arg value="-factory"/>
      <arg value="org.jikesrvm.tools.apt.SysCallProcessorFactory"/>
      <arg value="-classpath"/>
      <arg value="${tasks.classes}:${build.vmmagic-stub.classes}:${build.classes}"/>
      <arg value="-s"/>
      <arg value="${build.base}/syscall/java"/>
      <arg value="${main.java}/org/jikesrvm/runtime/SysCall.java"/>
    </exec>
    
8. Change the errorenous line with the following line
    <arg line="-Wl,--no-as-needed ${c++.args} ${rvm.common.args} ${gcspy.lib.dir} -lpthread ${perfctr.lib} ${c++.librt} ${rvm.src} ${c++.rdynamic} -g"/>

==================
Building ECJ
==================
0. jdk-6u14-linux-x64.bin

1. Change manifest:
Main-Class: org.eclipse.jdt.internal.compiler.batch.Main

2. Change Makefile
JIKES=../jikesrvm-3.1.0/dist/FastAdaptiveGCAssertions_x86_64-linux/rvm




export JAVA_HOME=/usr/lib/jvm/jdk1.5.0_22/
ant -Dconfig.name=BaseBaseGCAssertions -Dhost.name=x86_64-linux -Dcomponents.cache.dir=/home/ferhat/DeAL/temp
ant clean
ant -Dconfig.name=BaseBaseGCAssertions -Dhost.name=x86_64-linux -Dcomponents.cache.dir=/home/ferhat/DeAL/temp
ant -Dconfig.name=FastAdaptiveGCAssertions -Dhost.name=x86_64-linux -Dcomponents.cache.dir=/home/ferhat/DeAL/temp
make
make fastadaptive
make
cd ..
cd ecj-3.5.1/
cd ..
cd jikesrvm-3.1.0/
patch -p1 <ecj-3.5.1-gcassertions.diff 
cd ..
cd ecj-3.5.1/
patch -p1 <ecj-3.5.1-gcassertions.diff 
ant
export JAVA_HOME=/usr/lib/jvm/jdk1.6.0_31/

==================
Install Apache Ant on Ubuntu 16.04 manually
==================
[](https://howtoprogram.xyz/2016/10/14/install-apache-ant-ubuntu-16-04-lts-xenial-xerus/)


wget http://mirror.downloadvn.com/apache//ant/binaries/apache-ant-1.9.7-bin.tar.gz
sudo tar -xf apache-ant-1.9.7-bin.tar.gz  -C /usr/local
sudo ln -s /usr/local/apache-ant-1.9.7/ /usr/local/ant

Create a ant.sh file at /etc/profile.d folder (you can use vi with below command)

sudo gedit /etc/profile.d/ant.sh


Enter the follow content to the file:

export ANT_HOME=/usr/local/ant
export PATH=${ANT_HOME}/bin:${PATH}

source /etc/profile

======================
http://www.oracle.com/technetwork/java/archive-139210.html
======================


======================
Installing JDK
======================
[](https://gist.githubusercontent.com/senthil245/6093389/raw/04e4f15d9ca3a215a2bb09666c136af98825da63/JDK%25201.6%2520Installation%2520in%2520Ubuntu)

chmod +x jdk-6u31-linux-x64.bin
./jdk-6u31-linux-x64.bin
sudo mv jdk1.6.0_31 /usr/lib/jvm

sudo update-alternatives --install "/usr/bin/java" "java" \
"/usr/lib/jvm/jdk1.6.0_31/bin/java" 1
sudo update-alternatives --install "/usr/bin/javac" "javac" \
"/usr/lib/jvm/jdk1.6.0_31/bin/javac" 1
sudo update-alternatives --install "/usr/bin/javaws" "javaws" \
"/usr/lib/jvm/jdk1.6.0_31/bin/javaws" 1
sudo update-alternatives --config java
