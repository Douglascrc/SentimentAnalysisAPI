# Usa uma imagem base do Python oficial
FROM python:3.10-slim

# Define a pasta de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de requisitos para dentro do container
COPY requirements.txt .

# Instala as dependências
RUN pip install --no-cache-dir -r requirements.txt

# Copia todo o resto do seu código para dentro do container
COPY . .

# Comando para rodar a aplicação quando o container subir
# (Ajuste 'app:app' se o seu arquivo principal não se chamar app.py)
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "3000"]