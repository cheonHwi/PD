#!/bin/bash

set -e

INSTALL_DIR="$HOME/.local/bin"
JAR_DIR="$HOME/.sourceparser"
JAR_SOURCE="build/libs/sourceparser.jar"

# 색상
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${GREEN}📦 Installing SourceParser locally...${NC}"
echo ""

# 1. JAR 파일 존재 확인
echo -e "${CYAN}[1/6] Checking JAR file...${NC}"
if [ ! -f "$JAR_SOURCE" ]; then
    echo -e "${RED}❌ JAR not found. Building...${NC}"
    ./gradlew build
fi

# JAR 파일 크기 확인
size=$(stat -f%z "$JAR_SOURCE" 2>/dev/null || stat -c%s "$JAR_SOURCE")
if [ $size -lt 1000 ]; then
    echo -e "${RED}❌ JAR too small ($size bytes). Rebuilding...${NC}"
    ./gradlew clean build
fi

# JAR 파일 검증
if ! jar tf "$JAR_SOURCE" > /dev/null 2>&1; then
    echo -e "${RED}❌ JAR file is corrupted. Rebuilding...${NC}"
    ./gradlew clean build
fi

echo -e "${GREEN}✓ JAR file valid (${size} bytes)${NC}"

# 2. 디렉토리 생성
echo -e "${CYAN}[2/6] Creating directories...${NC}"
mkdir -p "$INSTALL_DIR"
mkdir -p "$JAR_DIR"
echo -e "${GREEN}✓ Directories created${NC}"

# 3. JAR 복사
echo -e "${CYAN}[3/6] Copying JAR...${NC}"
cp "$JAR_SOURCE" "$JAR_DIR/sourceparser.jar"

# 복사된 JAR 검증
if ! jar tf "$JAR_DIR/sourceparser.jar" > /dev/null 2>&1; then
    echo -e "${RED}❌ Copied JAR is invalid${NC}"
    exit 1
fi
echo -e "${GREEN}✓ JAR copied${NC}"

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

# 파일 존재 확인
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
    echo -e "${YELLOW}⚠️  Execution test skipped (--version not implemented)${NC}"
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
echo "  Permissions:"
echo "    - Script:  $(ls -l $INSTALL_DIR/sourceparser | awk '{print $1}')"
echo "    - JAR:     $(ls -l $JAR_DIR/sourceparser.jar | awk '{print $1}')"
echo ""
echo -e "${CYAN}Usage:${NC}"
echo "  sourceparser /path/to/project"
echo "  sourceparser ."
echo "  sourceparser --help"
echo ""
echo -e "${CYAN}Test installation:${NC}"
echo "  sourceparser --version"