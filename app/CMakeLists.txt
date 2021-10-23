cmake_minimum_required(VERSION 3.4.1)

set(APP_DIR src/main/cpp)
file(GLOB_RECURSE APP_SOURCES
        ${APP_DIR}/native-lib.cpp
        ${APP_DIR}/ClickPlayer.cpp
        ${APP_DIR}/SoundRenderer.cpp
        ${APP_DIR}/Mixer.cpp
        )
# Build our own native library
add_library(native-lib SHARED ${APP_SOURCES})

# Find the Oboe package
find_package(oboe REQUIRED CONFIG)

# Specify the libraries which our native library is dependent on, including Oboe
target_link_libraries(native-lib android log oboe::oboe)