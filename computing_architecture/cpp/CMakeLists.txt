cmake_minimum_required(VERSION 3.17)
project(Comparch)

set(CMAKE_CXX_STANDARD 20)

include_directories("/usr/local/include" "/usr/local/opt/llvm/include")
link_directories("/usr/local/lib" "/usr/local/opt/llvm/lib")

add_executable(cpp hw4/hw4.cpp)