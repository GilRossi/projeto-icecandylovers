resource "aws_s3_bucket" "bucket"{
  bucket = var.bucket_name
}

# Criar uma VPC
resource "aws_vpc" "vpc_default" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = var.vpc_name
  }
}

# Criar uma Subnet Pública
resource "aws_subnet" "public_subnet" {
  vpc_id            = aws_vpc.vpc_default.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "us-east-1a" # Altere para a zona de disponibilidade desejada

  tags = {
    Name = var.subnet_name
  }
}

# Criar um Internet Gateway
resource "aws_internet_gateway" "internet_gateway" {
  vpc_id = aws_vpc.vpc_default.id

  tags = {
    Name = var.internet_gateway_name
  }
}

# Criar uma Tabela de Roteamento Pública
resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.vpc_default.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.internet_gateway.id
  }

  tags = {
    Name = var.public_route_table_name
  }
}

# Associar a Subnet Pública à Tabela de Roteamento Pública
resource "aws_route_table_association" "public_subnet_association" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_route_table.id
}

# Criar um Security Group para permitir SSH
resource "aws_security_group" "ssh_access" {
  vpc_id = aws_vpc.vpc_default.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Permite SSH de qualquer IP (não recomendado para produção)
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = var.ssh_access_name
  }
}

# Criar uma Instância EC2
resource "aws_instance" "icecandylovers_ec2" {
  ami           = "ami-0c02fb55956c7d316" # AMI do Amazon Linux 2 (altere conforme necessário)
  instance_type = "t2.micro"
  subnet_id     = aws_subnet.public_subnet.id
  key_name      = "my-key-pair" # Substitua pelo nome do seu par de chaves SSH

  vpc_security_group_ids = [aws_security_group.ssh_access.id]

  associate_public_ip_address = true # Atribuir um IP público

  tags = {
    Name = var.icecandylovers_ec2_name
  }
}

# Exibir o IP público da instância EC2
output "public_ip" {
  value = aws_instance.icecandylovers_ec2.public_ip
}