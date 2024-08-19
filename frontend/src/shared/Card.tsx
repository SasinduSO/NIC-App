import React from 'react';

interface CardProps {
  title?: string;
  children: React.ReactNode;
  className?: string; // Add this line
}

const Card: React.FC<CardProps> = ({ title, children, className }) => {
  return (
    <div className={`bg-white rounded-lg shadow-lg p-6 m-4 ${className}`} style={{ maxWidth: '400px' }}>
      {title && <h3 className="text-xl font-semibold mb-4">{title}</h3>}
      {children}
    </div>
  );
};

export default Card;
