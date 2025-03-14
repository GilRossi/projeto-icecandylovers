provider "aws" {
  region = var.aws_region
}

# Gerar uma chave SSH dinamicamente
resource "tls_private_key" "example" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

# Criar o par de chaves na AWS
resource "aws_key_pair" "my_key_pair" {
  key_name   = "my-key-name"
  public_key = tls_private_key.example.public_key_openssh
}

# Cria uma nova VPC
resource "aws_vpc" "my_vpc" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "my-vpc"
  }
}

# Cria uma nova Subnet
resource "aws_subnet" "my_subnet" {
  vpc_id            = aws_vpc.my_vpc.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "us-east-1a"

  tags = {
    Name        = "my-subnet"
    Environment = "development"
  }
}

# Cria um Security Group para a instância
resource "aws_security_group" "java_sg" {
  name_prefix = "java-sg"
  vpc_id      = aws_vpc.my_vpc.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "java-sg"
    Environment = "development"
  }
}

# Cria uma nova instância EC2
resource "aws_instance" "java_app" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = "t2.micro"
  key_name               = aws_key_pair.my_key_pair.key_name
  subnet_id              = aws_subnet.my_subnet.id
  vpc_security_group_ids = [aws_security_group.java_sg.id]

  tags = {
    Name        = "Java21App"
    Environment = "development"
  }
}

# Busca a AMI mais recente do Ubuntu
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]  # ID do proprietário para imagens oficiais do Ubuntu
  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"]
  }
}