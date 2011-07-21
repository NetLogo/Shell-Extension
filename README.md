# NetLogo shell extension

This package contains the NetLogo shell extension.

It lets you do things like:

> (Mac) `observer> show shell:exec "ls"`

> (Windows) `observer> show (shell:exec "cmd" "/c" "dir")`

The included commands are:

 * `shell:pwd` => reports current working directory
 * `shell:cd <directory>` => change current working directory (relative to current directory unless <directory> begins with a drive letter on Windows or a forward slash on Mac/Unix)
 * `shell:getenv <name>` => reports value of environment variable
 * `shell:setenv <name> <value>` => sets environment variable
 * `shell:exec <command> (shell:exec <command> <param> ...)` => execute command synchronously and report a string of the results it prints to stdout
 * `shell:fork <command> (shell:fork <command> <param> ...)` => execute command asynchronously and discard the results

## Using

Download and unzip to the extensions folder inside your NetLogo program folder.

Ffor more information about NetLogo extensions in general, see the NetLogo User Manual.

## Building

Use the NETLOGO environment variable to tell the Makefile which NetLogo.jar to compile against.  For example:

    NETLOGO=/Applications/NetLogo\\\ 5.0 make

If compilation succeeds, `shell.jar` will be created.

## Terms of Use

All contents © 2011 Eric Russell

The contents of this package may be freely copied, distributed, altered, or otherwise used by anyone for any legal purpose.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
