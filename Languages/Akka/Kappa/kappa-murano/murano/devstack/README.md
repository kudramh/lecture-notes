## DevStack local.conf with Murano and Ceilometer

###To Install DevStack with Murano and Ceilometer on Ubuntu 14.04.2

#### How to Setup

Install Ubuntu as your normally would and make sure SSH is enabled

SSH into Ubuntu and login and run the following:

```
sudo apt-get update && sudo apt-get install git -y
```

#### Create the "stack" user to install DevStack

```
git clone https://github.com/openstack-dev/devstack
```

#### Create the "stack" user required to install DevStack

```
sudo ./devstack/tools/create-stack-user.sh
```

#### Create a password for the "stack" user

```
sudo passwd stack
```

#### Switch from "root" to "stack"

```
su stack
```

#### Then...

```
cd
```

#### Download DevStack again as the "stack" user

```
git clone https://github.com/openstack-dev/devstack
```

```
cd devstack
```

#### Download the local.conf file preconfigured with Murano. Change the settings to your liking. 

```
wget https://raw.githubusercontent.com/jolsenatx/devstack-murano/master/local.conf
```

#### Install DevStack

```
./stack.sh
```

#### Upload Ubuntu 14.04 x64 (pre-installed murano-agent) (qcow2) to Glance

ssh into DevStack instance

```
cd/devstack
```

```
source openrc
```

```
glance image-create --copy-from http://storage.apps.openstack.org/images/ubuntu-14.04-m-agent.qcow2 --disk-format qcow2 --container-format bare --name 'Ubuntu 14.04 x64 (pre-installed murano-agent)'
```

#### Troubleshooting

You may need to change your GIT_BASE in "stackrc" if you are installing behind a firewall

```
nano ./devstack/stackrc
```

```
Make change in stackrc :

GIT_BASE=${GIT_BASE:-git://git.openstack.org}

to:

GIT_BASE=${GIT_BASE:-https://www.github.com}
```

#### Rebuilding the stack

```
cd devstack
```

```
./unstack.sh
```

Then...

```
./stack.sh
```
