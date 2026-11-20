import zipfile
import xml.etree.ElementTree as ET
import re

def extract_text_from_docx(docx_path):
    text = ""
    try:
        with zipfile.ZipFile(docx_path, 'r') as zip_ref:
            for name in zip_ref.namelist():
                if name == 'word/document.xml':
                    with zip_ref.open(name) as f:
                        xml_content = f.read().decode('utf-8')
                        root = ET.fromstring(xml_content)
                        ns = {'w': 'http://schemas.openxmlformats.org/wordprocessingml/2006/main'}
                        for elem in root.iter('{http://schemas.openxmlformats.org/wordprocessingml/2006/main}t'):
                            if elem.text:
                                text += elem.text + '\n'
                    break
    except Exception as e:
        text = f"Error: {e}"
    return text

def extract_text_from_doc(doc_path):
    try:
        with open(doc_path, 'rb') as f:
            content = f.read()
            try:
                text = content.decode('gbk', errors='replace')
            except:
                try:
                    text = content.decode('utf-8', errors='replace')
                except:
                    text = content.decode('latin-1', errors='replace')
            text = re.sub(r'[\x00-\x08\x0b\x0c\x0e-\x1f\x7f-\xff]', '', text)
            return text
    except Exception as e:
        return f"Error: {e}"

print("=== 1.docx ===")
docx_text = extract_text_from_docx('1.docx')
print(docx_text[:15000])

print("\n\n=== 2.1.doc ===")
doc_text = extract_text_from_doc('2.1.doc')
print(doc_text[:15000])
