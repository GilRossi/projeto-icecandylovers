provider "aws" {
  region = var.aws_region
}

# Criar uma VPC
resource "aws_vpc" "my_vpc" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "my-vpc"
  }
}

# Criar uma Subnet pública
resource "aws_subnet" "my_subnet" {
  vpc_id            = aws_vpc.my_vpc.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "us-east-1a"
  map_public_ip_on_launch = true  # Atribui um IP público automaticamente

  tags = {
    Name        = "my-subnet"
    Environment = "development"
  }
}

# Criar um Internet Gateway
resource "aws_internet_gateway" "my_igw" {
  vpc_id = aws_vpc.my_vpc.id

  tags = {
    Name = "my-igw"
  }
}

# Criar uma Tabela de Rotas pública
resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.my_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.my_igw.id
  }

  tags = {
    Name = "public-route-table"
  }
}

# Associar a Subnet pública à Tabela de Rotas
resource "aws_route_table_association" "public_subnet_association" {
  subnet_id      = aws_subnet.my_subnet.id
  route_table_id = aws_route_table.public_route_table.id
}

# Criar um Security Group
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

# Criar uma instância EC2
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

# Buscar a AMI mais recente do Ubuntu
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]  # ID do proprietário para imagens oficiais do Ubuntu
  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"]
  }
}