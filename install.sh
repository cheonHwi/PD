#!/bin/bash

set -e

VERSION="1.0.0"
GITHUB_REPO="https://github.com/cheonHwi/PD"
JAR_URL="https://github.com/$GITHUB_REPO/releases/download/v${VERSION}/sourceparser.jar"

INSTALL_DIR="$HOME/.local/bin"
JAR_DIR="$HOME/.sourceparser"

# 색상
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${GREEN}📦 Installing SourceParser v${VERSION}...${NC}"
echo ""

# 1. Java 확인
echo -e "${CYAN}[1/6] Checking Java...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java not found. Please install Java 17+${NC}"
    exit 1
fi
java_version=$(java -version 2>&1 | head -n 1)
echo -e "${GREEN}✓ Java found: ${java_version}${NC}"

# 2. 디렉토리 생성
echo -e "${CYAN}[2/6] Creating directories...${NC}"
mkdir -p "$INSTALL_DIR"
mkdir -p "$JAR_DIR"
echo -e "${GREEN}✓ Directories created${NC}"

# 3. JAR 다운로드
echo -e "${CYAN}[3/6] Downloading sourceparser.jar...${NC}"
if command -v curl &> /dev/null; then
    curl -L "$JAR_URL" -o "$JAR_DIR/sourceparser.jar"
elif command -v wget &> /dev/null; then
    wget "$JAR_URL" -O "$JAR_DIR/sourceparser.jar"
else
    echo -e "${RED}❌ Neither curl nor wget found${NC}"
    exit 1
fi

# 다운로드 검증
if [ ! -f "$JAR_DIR/sourceparser.jar" ]; then
    echo -e "${RED}❌ Download failed${NC}"
    exit 1
fi

size=$(stat -f%z "$JAR_DIR/sourceparser.jar" 2>/dev/null || stat -c%s "$JAR_DIR/sourceparser.jar")
if [ $size -lt 1000 ]; then
    echo -e "${RED}❌ Downloaded file is too small ($size bytes)${NC}"
    rm "$JAR_DIR/sourceparser.jar"
    exit 1
fi

echo -e "${GREEN}✓ Downloaded (${size} bytes)${NC}"

# 4. 실행 스크립트 생성
echo -e "${CYAN}[4/6] Creating executable script...${NC}"
cat > "$INSTALL_DIR/sourceparser" << 'EOF'
#!/bin/bash
exec java -jar "$HOME/.sourceparser/sourceparser.jar" "$@"
EOF
echo -e "${GREEN}✓ Script created${NC}"

# 5. 실행 권한 부여
echo -e "${CYAN}[5/6] Setting permissions...${NC}"

# JAR 파일 권한
chmod 644 "$JAR_DIR/sourceparser.jar"
echo -e "${GREEN}✓ JAR permissions: 644${NC}"

# 실행 스크립트 권한
chmod +x "$INSTALL_DIR/sourceparser"
echo -e "${GREEN}✓ Script permissions: 755${NC}"

# 권한 검증
if [ ! -x "$INSTALL_DIR/sourceparser" ]; then
    echo -e "${RED}❌ Failed to set execute permission${NC}"
    exit 1
fi

# 6. 설치 검증
echo -e "${CYAN}[6/6] Verifying installation...${NC}"

if [ -f "$JAR_DIR/sourceparser.jar" ] && [ -x "$INSTALL_DIR/sourceparser" ]; then
    echo -e "${GREEN}✓ All files installed correctly${NC}"
else
    echo -e "${RED}❌ Installation verification failed${NC}"
    exit 1
fi

# 실행 테스트
if "$INSTALL_DIR/sourceparser" --version > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Execution test passed${NC}"
else
    echo -e "${YELLOW}⚠️  Execution test skipped${NC}"
fi

echo ""
echo -e "${GREEN}✨ SourceParser installed successfully!${NC}"
echo ""

# PATH 확인
if [[ ":$PATH:" != *":$INSTALL_DIR:"* ]]; then
    echo -e "${YELLOW}⚠️  Add to your PATH:${NC}"
    echo "  export PATH=\"\$HOME/.local/bin:\$PATH\""
    echo ""
    echo "Add this to your ~/.bashrc or ~/.zshrc, then run:"
    echo "  source ~/.bashrc  # or source ~/.zshrc"
    echo ""
else
    echo -e "${GREEN}✓ Already in PATH${NC}"
    echo ""
fi

echo -e "${CYAN}Installation details:${NC}"
echo "  Executable: $INSTALL_DIR/sourceparser"
echo "  JAR:        $JAR_DIR/sourceparser.jar"
echo "  Version:    v$VERSION"
echo ""
echo -e "${CYAN}Usage:${NC}"
echo "  sourceparser /path/to/project"
echo "  sourceparser ."
echo "  sourceparser --help"
echo ""
echo -e "${CYAN}Uninstall:${NC}"
echo "  rm $INSTALL_DIR/sourceparser"
echo "  rm -rf $JAR_DIR"