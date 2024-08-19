import React from 'react';

interface Card2Props {
  title: string;
  children: React.ReactNode;
}

const Card2: React.FC<Card2Props> = ({ title, children }) => {
  return (
    <div className="card2  bg-opacity-50" style={{ padding: '20px', borderRadius: '10px', backgroundColor: '#D1E9F6', boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)', width: '90%', margin: '0 auto' }}>
      <div className="card2-title" style={{ marginBottom: '20px', fontWeight: 'bold', fontSize: '1.5rem' }}>
        {title}
      </div>
      <div className="card2-content">
        {children}
      </div>
    </div>
  );
};

export default Card2;
