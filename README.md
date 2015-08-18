# SQL-Alchemist

This is the Repository of the SQL-Alchemist project

## Requirements

- nodejs >= 0.12.7
- grunt-cli
- Typesafe activator >= 1.3.5

## Installation
```bash
# Download the Repository
git clone https://github.com/ifis-tu-bs/SQL-Alchemist-Teamprojekt.git
```

## Usage

We need to compile and move the front-end sources to our back-end application

```bash
# switch to the front-end directory
cd front-end

# install all node-modules
npm install

# with this command the source code will be compiled and moved
grunt move

# switching back to the repository-root
cd ..
```
now we should do the same with the source of the admin-tool

```bash
# switch to the admin-tool directory
cd admin-tool

# install all node-modules
npm install

# compile & move the source code
grunt move

# switching back to the repository-root
cd ..
```

### 1. Running development mode

```bash
# switch to the back-end directory
cd back-end

# launching the project
activator run
```

### 2. how to packaging the project

```bash
# switch to the back-end directory
cd back-end

# packaging the
activator compile stage dist
```
## API Reference

A detailed description can be found in the [API directory](API)

## License

The license is placed in the [LICENSE.md](LICENSE.md)
