{ pkgs ? import (builtins.fetchTarball {
    url = "https://github.com/NixOS/nixpkgs/archive/refs/tags/25.05.tar.gz";
    sha256 = "1915r28xc4znrh2vf4rrjnxldw2imysz819gzhk9qlrkqanmfsxd";
  }) { } }:

pkgs.mkShell {
  JAVA_HOME = pkgs.jdk21.home;

  packages = with pkgs; [
    jdk21
    jdt-language-server
    maven
  ];

  shellHook = ''
    echo "Java $(java -version 2>&1 | head -n 1)"
    echo "Maven $(mvn --version | head -n 1)"
  '';
}
